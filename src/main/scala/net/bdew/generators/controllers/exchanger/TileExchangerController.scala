package net.bdew.generators.controllers.exchanger

import net.bdew.generators.GeneratorsResourceProvider
import net.bdew.generators.config.{Config, ExchangerRecipeRegistry}
import net.bdew.generators.control.{CIControl, ControlActions}
import net.bdew.generators.registries.Modules
import net.bdew.generators.sensor.Sensors
import net.bdew.lib.capabilities.helpers.fluid.{FluidHandlerNull, FluidMultiHandler, RestrictedFluidHandler}
import net.bdew.lib.capabilities.helpers.items.{ItemMultiHandler, RestrictedItemHandler}
import net.bdew.lib.data.base.UpdateKind
import net.bdew.lib.data.{DataSlotDouble, DataSlotMovingAverage}
import net.bdew.lib.multiblock.ResourceProvider
import net.bdew.lib.multiblock.interact._
import net.bdew.lib.multiblock.tile.TileControllerGui
import net.bdew.lib.resource._
import net.bdew.lib.sensors.SensorSystem
import net.bdew.lib.sensors.multiblock.CIRedstoneSensors
import net.bdew.lib.tile.TileExtended
import net.bdew.lib.{Misc, Text}
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.{Inventory, Player}
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.block.entity.{BlockEntity, BlockEntityType}
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.fluids.capability.IFluidHandler
import net.minecraftforge.items.IItemHandler

class TileExchangerController(teType: BlockEntityType[_], pos: BlockPos, state: BlockState) extends TileExtended(teType, pos, state)
  with TileControllerGui with CIFluidInput with CIOutputFaces with CIFluidOutputSelect with CIItemOutput with CIItemInput with CIRedstoneSensors with CIControl {

  val cfg: ConfigExchanger = Config.HeatExchanger
  val resources: ResourceProvider = GeneratorsResourceProvider

  val heaterIn = new DataSlotResource("heaterIn", this, cfg.internalTankCapacity(), canAccept = ExchangerRecipeRegistry.isValidHeater)
  val coolerIn = new DataSlotResource("coolerIn", this, cfg.internalTankCapacity(), canAccept = ExchangerRecipeRegistry.isValidCooler)
  val heaterOut = new DataSlotResource("heaterOut", this, cfg.internalTankCapacity())
  val coolerOut = new DataSlotResource("coolerOut", this, cfg.internalTankCapacity())
  val heat: DataSlotDouble = DataSlotDouble("heat", this).setUpdate(UpdateKind.SAVE, UpdateKind.GUI)
  val maxHeatTransfer: DataSlotDouble = DataSlotDouble("maxHeatTransfer", this).setUpdate(UpdateKind.SAVE, UpdateKind.GUI)

  val inputRate: DataSlotMovingAverage = DataSlotMovingAverage("inputRate", this, 20)
  val outputRate: DataSlotMovingAverage = DataSlotMovingAverage("outputRate", this, 20)
  val lastInput: DataSlotResourceKindOption = DataSlotResourceKindOption("lastInputRes", this)
  val lastOutput: DataSlotResourceKindOption = DataSlotResourceKindOption("lastOutputRes", this)
  val heatLoss: DataSlotMovingAverage = DataSlotMovingAverage("heatLoss", this, 20)

  lazy val maxOutputs = 6

  serverTick.listen(() => {
    var tickInput = 0D
    var tickOutput = 0D

    // first use heat
    if (getControlStateWithDefault(ControlActions.exchangeHeat, true) && heat > cfg.startHeating() && coolerIn.resource.isDefined) {
      val transfer = Misc.clamp(heat.value - cfg.startHeating(), 0D, maxHeatTransfer.value)
      for {
        cooler <- coolerIn.resource
        rec <- ExchangerRecipeRegistry.findCooler(cooler)
      } {
        val maxFill = coolerOut.fill(rec.output.resource(transfer * rec.outPerHU), false, false)
        val toDrain = Misc.min(maxFill / rec.outPerHU * rec.inPerHU, transfer * rec.inPerHU, cooler.amount)
        if (toDrain > 0) {
          coolerIn.drain(toDrain, false, true)
          coolerOut.fill(rec.output.resource(toDrain / rec.inPerHU * rec.outPerHU), false, true)
          tickOutput = toDrain / rec.inPerHU * rec.outPerHU
          if (!lastOutput.contains(rec.output.resourceKind)) {
            lastOutput.set(rec.output.resourceKind)
            outputRate.values.clear()
          }
          heat -= toDrain / rec.inPerHU
        }
      }
    }

    // head decay is applied now to penalize overheated small exchanger and inactive exchangers in general
    if (heat > 0) {
      heatLoss.update(heat * cfg.heatDecay())
      heat := heat * (1 - cfg.heatDecay())
    } else {
      heatLoss.update(0)
    }

    // and finally restore heat
    if (getControlStateWithDefault(ControlActions.exchangeHeat, true) && heat < cfg.maxHeat() && heaterIn.resource.isDefined) {
      val transfer = Misc.clamp(cfg.maxHeat() - heat.value, 0D, maxHeatTransfer.value)
      for {
        heater <- heaterIn.resource
        rec <- ExchangerRecipeRegistry.findHeater(heater)
      } {
        val maxFill = heaterOut.fill(rec.output.resource(transfer * rec.outPerHU), false, false)
        val toDrain = Misc.min(maxFill / rec.outPerHU * rec.inPerHU, transfer * rec.inPerHU, heater.amount)
        if (toDrain > 0) {
          heaterIn.drain(toDrain, false, true)
          heaterOut.fill(rec.output.resource(toDrain / rec.inPerHU * rec.outPerHU), false, true)
          tickInput = toDrain
          if (!lastInput.contains(heater.kind)) {
            lastInput.set(heater.kind)
            inputRate.values.clear()
          }
          heat += toDrain / rec.inPerHU
        }
      }
    }

    inputRate.update(tickInput)
    outputRate.update(tickOutput)
  })

  override def onModulesChanged(): Unit = {
    maxHeatTransfer := getNumOfModules(Modules.heatExchanger) * Config.Modules.heatExchanger.heatTransfer()
    super.onModulesChanged()
  }

  override def getDisplayName: Component = Text.translate("advgenerators.gui.exchanger.title")
  override def createMenu(id: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu =
    new ContainerExchanger(this, playerInventory, id)

  override val itemInput: IItemHandler = RestrictedItemHandler.insertOnly(
    ItemMultiHandler.wrap(
      new ResourceItemHandler(heaterIn),
      new ResourceItemHandler(coolerIn),
    )
  )

  override val itemOutput: IItemHandler = RestrictedItemHandler.extractOnly(
    ItemMultiHandler.wrap(
      new ResourceItemHandler(heaterOut),
      new ResourceItemHandler(coolerOut),
    )
  )

  override val fluidInput: IFluidHandler = RestrictedFluidHandler.fillOnly(
    FluidMultiHandler.wrap(
      new ResourceFluidHandler(heaterIn),
      new ResourceFluidHandler(coolerIn),
    )
  )

  val fluidOutputHot: IFluidHandler = RestrictedFluidHandler.drainOnly(new ResourceFluidHandler(coolerOut))
  val fluidOutputCold: IFluidHandler = RestrictedFluidHandler.drainOnly(new ResourceFluidHandler(heaterOut))
  val fluidOutputBoth: IFluidHandler = FluidMultiHandler.wrap(fluidOutputHot, fluidOutputCold)

  override val outputSlotsDef: OutputSlotsExchanger.type = OutputSlotsExchanger

  override def fluidOutputForSlot(slot: outputSlotsDef.Slot): IFluidHandler = slot match {
    case OutputSlotsExchanger.BOTH => fluidOutputBoth
    case OutputSlotsExchanger.COLD => fluidOutputCold
    case OutputSlotsExchanger.HOT => fluidOutputHot
    case _ => FluidHandlerNull
  }

  override def redstoneSensorsType: List[Sensors.SimpleSensor] = Sensors.exchangerSensors
  override def redstoneSensorSystem: SensorSystem[BlockEntity, Boolean] = Sensors

  override def availableControlActions = List(ControlActions.disabled, ControlActions.exchangeHeat)
}
