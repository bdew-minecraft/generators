/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.controllers.turbine

import net.bdew.generators.config.{Modules, TurbineFuel}
import net.bdew.generators.control.{CIControl, ControlActions}
import net.bdew.generators.controllers.PoweredController
import net.bdew.generators.modules.efficiency.{BlockEfficiencyUpgradeTier1, BlockEfficiencyUpgradeTier2}
import net.bdew.generators.modules.powerCapacitor.BlockPowerCapacitor
import net.bdew.generators.modules.turbine.BlockTurbine
import net.bdew.generators.sensor.Sensors
import net.bdew.generators.{Generators, GeneratorsResourceProvider}
import net.bdew.lib.Misc
import net.bdew.lib.data._
import net.bdew.lib.data.base.UpdateKind
import net.bdew.lib.multiblock.interact.{CIFluidInput, CIOutputFaces, CIPowerProducer}
import net.bdew.lib.multiblock.tile.{TileControllerGui, TileModule}
import net.bdew.lib.power.DataSlotPower
import net.bdew.lib.sensors.multiblock.CIRedstoneSensors
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fluids.{Fluid, FluidStack}

class TileTurbineController extends TileControllerGui with PoweredController with CIFluidInput with CIOutputFaces with CIPowerProducer with CIRedstoneSensors with CIControl {
  val cfg = MachineTurbine

  val resources = GeneratorsResourceProvider

  val fuel = new DataSlotTank("fuel", this, 0)
  val power = new DataSlotPower("power", this)

  val maxMJPerTick = new DataSlotFloat("maxMJPerTick", this).setUpdate(UpdateKind.SAVE, UpdateKind.GUI)
  val burnTime = new DataSlotFloat("burnTime", this).setUpdate(UpdateKind.SAVE)

  val numTurbines = new DataSlotInt("turbines", this).setUpdate(UpdateKind.GUI)
  val fuelPerTick = new DataSlotFloat("fuelPerTick", this).setUpdate(UpdateKind.GUI)

  val fuelEfficiency = new DataSlotFloat("fuelConsumptionMultiplier", this).setUpdate(UpdateKind.SAVE, UpdateKind.GUI)

  val outputAverage = new DataSlotMovingAverage("outputAverage", this, 20)
  val fuelPerTickAverage = new DataSlotMovingAverage("fuelPerTickAverage", this, 20)

  override val redstoneSensorsType = Sensors.fuelTurbineSensors
  override val redstoneSensorSystem = Sensors

  lazy val maxOutputs = 6

  def doUpdate() {
    val fuelPerMj = if (fuel.getFluidAmount > 0) 1 / TurbineFuel.getFuelValue(fuel.getFluid.getFluid) / fuelEfficiency else 0
    fuelPerTick := fuelPerMj * maxMJPerTick

    if (getControlStateWithDefault(ControlActions.useFuel, true)) {
      if (burnTime < 5 && fuelPerMj > 0 && maxMJPerTick > 0) {
        val needFuel = Misc.clamp((10 * fuelPerTick).ceil, 0F, fuel.getFluidAmount.toFloat).floor.toInt
        burnTime += needFuel / fuelPerTick
        fuel.drain(needFuel, true)
        fuelPerTickAverage.update(needFuel)
      } else {
        fuelPerTickAverage.update(0)
      }

      if (burnTime > 1 && power.capacity - power.stored > maxMJPerTick) {
        burnTime -= 1
        power.stored += maxMJPerTick
        outputAverage.update(maxMJPerTick.toDouble)
        lastChange = worldObj.getTotalWorldTime
      } else {
        outputAverage.update(0)
      }
    } else {
      outputAverage.update(0)
      fuelPerTickAverage.update(0)
    }
  }

  serverTick.listen(doUpdate)

  override def openGui(player: EntityPlayer) = player.openGui(Generators, cfg.guiId, worldObj, xCoord, yCoord, zCoord)

  def inputFluid(resource: FluidStack, doFill: Boolean): Int =
    if (canInputFluid(resource.getFluid)) fuel.fill(resource, doFill) else 0

  def canInputFluid(fluid: Fluid) = TurbineFuel.isValidFuel(fluid)
  def getTankInfo = Array(fuel.getInfo)

  def extract(v: Float, simulate: Boolean) = power.extract(v, simulate)

  override def onModulesChanged() {
    fuel.setCapacity(getNumOfModules("FuelTank") * Modules.FuelTank.capacity + cfg.internalFuelCapacity)

    if (fuel.getFluid != null && fuel.getFluidAmount > fuel.getCapacity)
      fuel.getFluid.amount = fuel.getCapacity

    val capacitors = modules.toList.flatMap(_.getBlock[BlockPowerCapacitor](getWorldObj)).map(_.material)
    power.capacity = cfg.internalPowerCapacity + capacitors.map(_.mjCapacity).sum.toFloat

    if (power.stored > power.capacity)
      power.stored = power.capacity

    val turbines = modules.toList.flatMap(_.getBlock[BlockTurbine](getWorldObj)).map(_.material)
    maxMJPerTick := turbines.map(_.maxMJPerTick).sum.toFloat
    numTurbines := turbines.size

    val hasT1Upgrade = modules.exists(_.blockIs(worldObj, BlockEfficiencyUpgradeTier1))

    fuelEfficiency := modules.find(_.blockIs(worldObj, BlockEfficiencyUpgradeTier2)) map { t2 =>
      if (hasT1Upgrade) {
        MachineTurbine.fuelEfficiency.getFloat("Tier2")
      } else {
        t2.getTile[TileModule](worldObj).foreach { tile =>
          tile.coreRemoved()
          moduleRemoved(tile)
        }
        MachineTurbine.fuelEfficiency.getFloat("Base")
      }
    } getOrElse {
      if (hasT1Upgrade) {
        MachineTurbine.fuelEfficiency.getFloat("Tier1")
      } else {
        MachineTurbine.fuelEfficiency.getFloat("Base")
      }
    }

    super.onModulesChanged()
  }

  override def availableControlActions = List(ControlActions.disabled, ControlActions.useFuel)
}
