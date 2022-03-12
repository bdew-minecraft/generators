package net.bdew.generators.controllers.steam

import net.bdew.generators.GeneratorsResourceProvider
import net.bdew.generators.config.Config
import net.bdew.generators.control.{CIControl, ControlActions}
import net.bdew.generators.modules.powerCapacitor.BlockCapacitor
import net.bdew.generators.modules.turbine.BlockTurbine
import net.bdew.generators.registries.Fluids
import net.bdew.generators.sensor.Sensors
import net.bdew.lib.capabilities.handlers.PowerEnergyHandler
import net.bdew.lib.capabilities.helpers.fluid.{ConvertingFluidHandler, RestrictedFluidHandler}
import net.bdew.lib.data._
import net.bdew.lib.data.base.UpdateKind
import net.bdew.lib.multiblock.ResourceProvider
import net.bdew.lib.multiblock.interact.{CIFluidInput, CIOutputFaces, CIPowerOutput}
import net.bdew.lib.multiblock.tile.TileControllerGui
import net.bdew.lib.power.DataSlotPower
import net.bdew.lib.sensors.multiblock.CIRedstoneSensors
import net.bdew.lib.sensors.{GenericSensorType, SensorSystem}
import net.bdew.lib.tile.TileExtended
import net.bdew.lib.{Misc, Text}
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.FluidTags
import net.minecraft.world.entity.player.{Inventory, Player}
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.block.entity.{BlockEntity, BlockEntityType}
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.energy.IEnergyStorage
import net.minecraftforge.fluids.capability.IFluidHandler
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction

class TileSteamTurbineController(teType: BlockEntityType[_], pos: BlockPos, state: BlockState) extends TileExtended(teType, pos, state)
  with TileControllerGui with CIPowerOutput with CIFluidInput with CIOutputFaces with CIRedstoneSensors with CIControl {

  val cfg: ConfigSteamTurbine = Config.SteamTurbine
  val resources: ResourceProvider = GeneratorsResourceProvider

  val steam: DataSlotTankRestricted = DataSlotTankRestricted("steam", this, cfg.internalSteamCapacity(), Fluids.steam.source.get())

  val power: DataSlotPower = DataSlotPower("power", this)
  val speed: DataSlotDouble = DataSlotDouble("speed", this).setUpdate(UpdateKind.GUI, UpdateKind.SAVE)
  val numTurbines: DataSlotInt = DataSlotInt("turbines", this).setUpdate(UpdateKind.GUI)

  val inertiaMultiplier: DataSlotDouble = DataSlotDouble("inertiaMultiplier", this).setUpdate(UpdateKind.SAVE)
  val maxFEPerTick: DataSlotDouble = DataSlotDouble("maxFEPerTick", this).setUpdate(UpdateKind.SAVE, UpdateKind.GUI)

  val outputAverage: DataSlotMovingAverage = DataSlotMovingAverage("outputAverage", this, 20)
  val steamAverage: DataSlotMovingAverage = DataSlotMovingAverage("steamAverage", this, 20)

  def canGeneratePower: Boolean = getControlStateWithDefault(ControlActions.generateEnergy, power.stored < power.capacity)
  def canUseSteam: Boolean = getControlStateWithDefault(ControlActions.useSteam, true)

  lazy val maxOutputs = 6

  override val redstoneSensorsType: Seq[GenericSensorType[BlockEntity, Boolean]] = Sensors.steamTurbineSensors
  override val redstoneSensorSystem: SensorSystem[BlockEntity, Boolean] = Sensors

  serverTick.listen(() => {
    if (maxFEPerTick > 0) {
      val maxSpeedDelta = cfg.maxRPM() * cfg.inertiaFunctionMultiplier() * Math.exp(cfg.inertiaFunctionExponent() * speed / cfg.maxRPM()) * inertiaMultiplier

      if (canGeneratePower && speed > 1) {
        if (power.stored < power.capacity) {
          val injected = Math.min(speed / cfg.maxRPM() * maxFEPerTick, power.capacity - power.stored)
          power.stored += injected.toFloat
          outputAverage.update(injected)
        } else outputAverage.update(0)
        speed -= maxSpeedDelta * cfg.coilDragMultiplier()
      } else outputAverage.update(0)

      speed -= maxSpeedDelta * cfg.baseDragMultiplier()

      val maxSteamPerTick = maxFEPerTick / cfg.steamEnergyDensity()

      if (canUseSteam && steam.getFluidAmount > 0) {
        val useSteam = Math.min(steam.getFluidAmount, maxSteamPerTick).ceil.toInt
        steam.drain(useSteam, FluidAction.EXECUTE)
        steamAverage.update(useSteam)
      } else steamAverage.update(0)

      if (steamAverage.average > 0)
        speed := Misc.clamp(
          (steamAverage.average / maxSteamPerTick) * cfg.maxRPM(),
          speed.value,
          speed.value + (maxSpeedDelta * cfg.spinUpMultiplier())
        )

      if (speed < 1)
        speed := 0

    } else speed := 0
  })

  override def getDisplayName: Component = Text.translate("advgenerators.gui.turbine.steam.title")
  override def createMenu(id: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu =
    new ContainerSteamTurbine(this, playerInventory, id)

  override val powerOutput: IEnergyStorage = new PowerEnergyHandler(power, false, true)

  override val fluidInput: IFluidHandler =
    RestrictedFluidHandler.fillOnly(
      new ConvertingFluidHandler(steam,
        Fluids.steamTag,
        Fluids.steam.source.get()
      )
    )

  override def onModulesChanged(): Unit = {
    power.capacity = getModuleBlocks[BlockCapacitor].values.map(_.cfg.capacity()).sum.toFloat + cfg.internalEnergyCapacity()

    if (power.stored > power.capacity)
      power.stored = power.capacity

    val turbines = getModuleBlocks[BlockTurbine].values
    maxFEPerTick := turbines.map(_.cfg.maxFEPerTick()).sum.toFloat
    inertiaMultiplier := turbines.map(_.cfg.inertia()).sum / turbines.size
    numTurbines := turbines.size

    super.onModulesChanged()
  }

  override def availableControlActions = List(ControlActions.disabled, ControlActions.useSteam, ControlActions.generateEnergy)
}
