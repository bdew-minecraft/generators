package net.bdew.generators.controllers.turbine

import net.bdew.generators.GeneratorsResourceProvider
import net.bdew.generators.config.{Config, TurbineFuelRegistry}
import net.bdew.generators.control.{CIControl, ControlActions}
import net.bdew.generators.modules.powerCapacitor.BlockCapacitor
import net.bdew.generators.modules.turbine.BlockTurbine
import net.bdew.generators.registries.Modules
import net.bdew.generators.sensor.Sensors
import net.bdew.lib.capabilities.handlers.PowerEnergyHandler
import net.bdew.lib.capabilities.helpers.fluid.RestrictedFluidHandler
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
import net.minecraft.entity.player.{PlayerEntity, PlayerInventory}
import net.minecraft.inventory.container.Container
import net.minecraft.tileentity.{TileEntity, TileEntityType}
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.energy.IEnergyStorage
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.IFluidHandler
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction

class TileFuelTurbineController(teType: TileEntityType[_]) extends TileExtended(teType)
  with TileControllerGui with CIFluidInput with CIOutputFaces with CIPowerOutput with CIRedstoneSensors with CIControl {

  val cfg: ConfigFuelTurbine = Config.FuelTurbine
  val resources: ResourceProvider = GeneratorsResourceProvider

  val fuel: DataSlotTank = new DataSlotTank("fuel", this, 0) {
    override def isFluidValid(stack: FluidStack): Boolean = TurbineFuelRegistry.isValidFuel(stack)
  }

  val power: DataSlotPower = DataSlotPower("power", this)

  val maxFEPerTick: DataSlotFloat = DataSlotFloat("maxFEPerTick", this).setUpdate(UpdateKind.SAVE, UpdateKind.GUI)
  val burnTime: DataSlotFloat = DataSlotFloat("burnTime", this).setUpdate(UpdateKind.SAVE)

  val numTurbines: DataSlotInt = DataSlotInt("turbines", this).setUpdate(UpdateKind.GUI)
  val fuelPerTick: DataSlotFloat = DataSlotFloat("fuelPerTick", this).setUpdate(UpdateKind.GUI)

  val fuelEfficiency: DataSlotFloat = DataSlotFloat("fuelConsumptionMultiplier", this).setUpdate(UpdateKind.SAVE, UpdateKind.GUI)

  val outputAverage: DataSlotMovingAverage = DataSlotMovingAverage("outputAverage", this, 20)
  val fuelPerTickAverage: DataSlotMovingAverage = DataSlotMovingAverage("fuelPerTickAverage", this, 20)

  override val redstoneSensorsType: Seq[GenericSensorType[TileEntity, Boolean]] = Sensors.fuelTurbineSensors
  override val redstoneSensorSystem: SensorSystem[TileEntity, Boolean] = Sensors

  lazy val maxOutputs = 6

  def doUpdate(): Unit = {
    val fuelPerFE = if (fuel.getFluidAmount > 0) 1 / TurbineFuelRegistry.getFuelValue(fuel.getFluid.getFluid) / fuelEfficiency else 0
    fuelPerTick := fuelPerFE * maxFEPerTick

    if (getControlStateWithDefault(ControlActions.useFuel, true)) {
      if (burnTime < 5 && fuelPerFE > 0 && maxFEPerTick > 0) {
        val needFuel = Misc.clamp((10 * fuelPerTick).ceil, 0F, fuel.getFluidAmount.toFloat).floor.toInt
        burnTime += needFuel / fuelPerTick
        fuel.drain(needFuel, FluidAction.EXECUTE)
        fuelPerTickAverage.update(needFuel)
      } else {
        fuelPerTickAverage.update(0)
      }

      if (burnTime > 1 && power.capacity - power.stored > maxFEPerTick) {
        burnTime -= 1
        power.stored += maxFEPerTick
        outputAverage.update(maxFEPerTick.toDouble)
        lastChange = level.getGameTime
      } else {
        outputAverage.update(0)
      }
    } else {
      outputAverage.update(0)
      fuelPerTickAverage.update(0)
    }
  }

  serverTick.listen(doUpdate)

  override def getDisplayName: ITextComponent = Text.translate("advgenerators.gui.turbine.title")
  override def createMenu(id: Int, playerInventory: PlayerInventory, player: PlayerEntity): Container =
    new ContainerFuelTurbine(this, playerInventory, id)

  override val fluidInput: IFluidHandler = RestrictedFluidHandler.fillOnly(fuel)
  override val powerOutput: IEnergyStorage = new PowerEnergyHandler(power, false, true)

  override def onModulesChanged(): Unit = {
    fuel.setCapacity(getNumOfModules(Modules.fuelTank) * Config.Modules.fuelTank.capacity() + cfg.internalFuelCapacity())

    if (fuel.getFluid != null && fuel.getFluidAmount > fuel.getCapacity)
      fuel.getFluid.setAmount(fuel.getCapacity)

    power.capacity = getModuleBlocks[BlockCapacitor].values.map(_.cfg.capacity()).sum.toFloat + cfg.internalEnergyCapacity()

    if (power.stored > power.capacity)
      power.stored = power.capacity

    val turbines = getModuleBlocks[BlockTurbine].values
    maxFEPerTick := turbines.map(_.cfg.maxFEPerTick()).sum.toFloat
    numTurbines := turbines.size

    val hasT1Upgrade = getNumOfModules(Modules.efficiencyUpgradeTier1) > 0
    val hasT2Upgrade = getNumOfModules(Modules.efficiencyUpgradeTier2) > 0

    fuelEfficiency := ((hasT1Upgrade, hasT2Upgrade) match {
      case (true, true) => cfg.fuelEfficiencyTier2()
      case (true, false) => cfg.fuelEfficiencyTier1()
      case (false, _) => cfg.fuelEfficiencyBase()
    })

    super.onModulesChanged()
  }

  override def availableControlActions = List(ControlActions.disabled, ControlActions.useFuel)
}
