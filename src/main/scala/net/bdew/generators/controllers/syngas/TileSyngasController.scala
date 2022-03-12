package net.bdew.generators.controllers.syngas

import net.bdew.generators.GeneratorsResourceProvider
import net.bdew.generators.config.{CarbonValueRegistry, Config}
import net.bdew.generators.control.{CIControl, ControlActions}
import net.bdew.generators.registries.{Fluids, Modules}
import net.bdew.generators.sensor.Sensors
import net.bdew.lib.capabilities.handlers.InventoryItemHandler
import net.bdew.lib.capabilities.helpers.fluid.{ConvertingFluidHandler, FluidMultiHandler, RestrictedFluidHandler}
import net.bdew.lib.data._
import net.bdew.lib.data.base.UpdateKind
import net.bdew.lib.multiblock.ResourceProvider
import net.bdew.lib.multiblock.interact.{CIFluidInput, CIFluidOutputSelect, CIItemInput, CIOutputFaces}
import net.bdew.lib.multiblock.tile.TileControllerGui
import net.bdew.lib.sensors.multiblock.CIRedstoneSensors
import net.bdew.lib.sensors.{GenericSensorType, SensorSystem}
import net.bdew.lib.tile.{TankEmulator, TileExtended}
import net.bdew.lib.{Misc, Text}
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.FluidTags
import net.minecraft.world.entity.player.{Inventory, Player}
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.{BlockEntity, BlockEntityType}
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.{Fluids => VanillaFluids}
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.IFluidHandler
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction
import net.minecraftforge.items.IItemHandler

class TileSyngasController(teType: BlockEntityType[_], pos: BlockPos, state: BlockState) extends TileExtended(teType, pos, state)
  with TileControllerGui with CIFluidInput with CIItemInput with CIOutputFaces with CIFluidOutputSelect with CIRedstoneSensors with CIControl {

  override val cfg: ConfigSyngas = Config.SyngasProducer
  override val resources: ResourceProvider = GeneratorsResourceProvider
  override lazy val maxOutputs = 6

  val inventory: DataSlotInventory = new DataSlotInventory("inv", this, 4) {
    override def canPlaceItem(slot: Int, stack: ItemStack): Boolean = CarbonValueRegistry.isValid(stack)
  }

  val carbonBuffer: DataSlotDouble = DataSlotDouble("carbon", this).setUpdate(UpdateKind.GUI, UpdateKind.SAVE)
  val steamBuffer: DataSlotDouble = DataSlotDouble("steam", this).setUpdate(UpdateKind.GUI, UpdateKind.SAVE)
  val heat: DataSlotDouble = DataSlotDouble("heat", this).setUpdate(UpdateKind.GUI, UpdateKind.SAVE)
  val waterTank: DataSlotTankRestricted = DataSlotTankRestricted("waterTank", this, cfg.internalTankCapacity(), VanillaFluids.WATER)
  val syngasTank: DataSlotTankRestricted = DataSlotTankRestricted("syngasTank", this, cfg.internalTankCapacity(), Fluids.syngas.source.get())

  val steamTank = new ConvertingFluidHandler(
    TankEmulator(Fluids.steam.source.get(), steamBuffer, cfg.internalTankCapacity()),
    Fluids.steamTag,
    Fluids.steam.source.get()
  )

  val heatingChambers: DataSlotInt = DataSlotInt("heatingChambers", this).setUpdate(UpdateKind.SAVE, UpdateKind.GUI)
  val mixingChambers: DataSlotInt = DataSlotInt("mixingChambers", this)

  val avgCarbonUsed: DataSlotMovingAverage = DataSlotMovingAverage("carbonUsed", this, 20)
  val avgSyngasProduced: DataSlotMovingAverage = DataSlotMovingAverage("syngasProduced", this, 20)
  val avgHeatDelta: DataSlotMovingAverage = DataSlotMovingAverage("heatDelta", this, 20)

  serverTick.listen(() => {
    var carbonUsed = 0D
    var syngasProduced = 0D
    var heatDelta = 0D

    // Consume carbon to add heat
    if (heat < cfg.maxHeat() && carbonBuffer > 0 && heatingChambers > 0 && getControlStateWithDefault(ControlActions.heatWater, waterTank.getFluidAmount > 0)) {
      val addHeat = Misc.min(
        cfg.maxHeat() - heat,
        carbonBuffer / cfg.carbonPerHeat(),
        heatingChambers * cfg.heatingChamberHeating()
      )
      heat += addHeat
      carbonBuffer -= addHeat * cfg.carbonPerHeat()
      carbonUsed += addHeat * cfg.carbonPerHeat()
      heatDelta += addHeat
    }

    // Consume water to make steam
    if (heat > cfg.workHeat() && waterTank.getFluidAmount > 0 && steamBuffer < cfg.internalTankCapacity() && heatingChambers > 0) {
      val addSteam = Misc.min(
        waterTank.getFluidAmount * cfg.waterSteamRatio(),
        cfg.internalTankCapacity() - steamBuffer,
        heatingChambers * cfg.heatingChamberThroughput() * (heat / cfg.maxHeat())
      )
      steamBuffer += addSteam
      waterTank.drain((addSteam / cfg.waterSteamRatio()).ceil.toInt, FluidAction.EXECUTE)
    }

    // Consume steam and carbon to make syngas
    if (steamBuffer > 0 && carbonBuffer > 0 && syngasTank.getFluidAmount < syngasTank.getCapacity && getControlStateWithDefault(ControlActions.mix, true)) {
      val addSyngas = Misc.min[Double](
        carbonBuffer / cfg.carbonPerMBSyngas(),
        steamBuffer / cfg.steamPerMBSyngas(),
        syngasTank.getCapacity - syngasTank.getFluidAmount,
        cfg.mixingChamberThroughput() * mixingChambers
      ).floor.toInt
      carbonBuffer -= addSyngas * cfg.carbonPerMBSyngas()
      steamBuffer -= addSyngas * cfg.steamPerMBSyngas()
      syngasTank.fill(new FluidStack(Fluids.syngas.source.get(), addSyngas), FluidAction.EXECUTE)
      syngasProduced += addSyngas
      carbonUsed += addSyngas * cfg.carbonPerMBSyngas()
    }

    if (heat > 0) {
      val heatLoss = Misc.min(heat.value, heatingChambers * cfg.heatingChamberLoss())
      heat -= heatLoss
      heatDelta -= heatLoss
    }

    // Consume fuel to add carbon
    for {
      slot <- 0 until inventory.size
      stack <- Option(inventory.getItem(slot))
      cValue <- CarbonValueRegistry.getValueOpt(stack)
      if cfg.internalTankCapacity() - carbonBuffer >= cValue
    } {
      inventory.removeItem(slot, 1)
      carbonBuffer += cValue
    }

    avgCarbonUsed.update(carbonUsed)
    avgSyngasProduced.update(syngasProduced)
    avgHeatDelta.update(heatDelta)
  })

  override def onModulesChanged(): Unit = {
    heatingChambers := getNumOfModules(Modules.heatingChamber)
    mixingChambers := getNumOfModules(Modules.mixingChamber)
    super.onModulesChanged()
  }

  override def getDisplayName: Component = Text.translate("advgenerators.gui.syngas.title")
  override def createMenu(id: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu =
    new ContainerSyngas(this, playerInventory, id)

  override val itemInput: IItemHandler = new InventoryItemHandler(inventory, _ => false, (_, _) => true)
  override val fluidInput: IFluidHandler = RestrictedFluidHandler.fillOnly(FluidMultiHandler.wrap(waterTank, steamTank))

  override val outputSlotsDef: OutputSlotsSyngas.type = OutputSlotsSyngas
  override def fluidOutputForSlot(slot: OutputSlotsSyngas.Slot): IFluidHandler = RestrictedFluidHandler.drainOnly(syngasTank)

  override def redstoneSensorsType: Seq[GenericSensorType[BlockEntity, Boolean]] = Sensors.syngasSensors
  override def redstoneSensorSystem: SensorSystem[BlockEntity, Boolean] = Sensors

  override def availableControlActions = List(ControlActions.disabled, ControlActions.heatWater, ControlActions.mix)

  override def onBreak(): Unit = {
    super.onBreak()
    inventory.dropContent(level, getBlockPos)
  }
}
