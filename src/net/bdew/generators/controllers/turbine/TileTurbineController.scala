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
import net.bdew.generators.controllers.PoweredController
import net.bdew.generators.sensor.Sensors
import net.bdew.generators.{Generators, GeneratorsResourceProvider}
import net.bdew.lib.Misc
import net.bdew.lib.data.base.UpdateKind
import net.bdew.lib.data.{DataSlotFloat, DataSlotInt, DataSlotMovingAverage, DataSlotTank}
import net.bdew.lib.multiblock.interact.{CIFluidInput, CIOutputFaces, CIPowerProducer}
import net.bdew.lib.multiblock.tile.TileControllerGui
import net.bdew.lib.power.DataSlotPower
import net.bdew.lib.sensors.multiblock.CISensors
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fluids.{Fluid, FluidStack}

class TileTurbineController extends TileControllerGui with PoweredController with CIFluidInput with CIOutputFaces with CIPowerProducer with CISensors {
  val cfg = MachineTurbine

  val resources = GeneratorsResourceProvider

  val fuel = new DataSlotTank("fuel", this, 0)
  val power = new DataSlotPower("power", this)

  val mjPerTick = new DataSlotFloat("mjPerTick", this).setUpdate(UpdateKind.GUI, UpdateKind.SAVE)
  val burnTime = new DataSlotFloat("burnTime", this).setUpdate(UpdateKind.SAVE)

  val numTurbines = new DataSlotInt("turbines", this).setUpdate(UpdateKind.GUI)
  val fuelPerTick = new DataSlotFloat("fuelPerTick", this).setUpdate(UpdateKind.GUI)

  val outputAverage = new DataSlotMovingAverage("outputAverage", this, 20)
  val fuelPerTickAverage = new DataSlotMovingAverage("fuelPerTickAverage", this, 20)

  override val sensorTypes = Sensors.fuelTurbineSensors

  lazy val maxOutputs = 6

  def doUpdate() {
    val fuelPerMj = if (fuel.getFluidAmount > 0) 1 / TurbineFuel.getFuelValue(fuel.getFluid.getFluid) * cfg.fuelConsumptionMultiplier else 0
    fuelPerTick := fuelPerMj * mjPerTick

    if (burnTime < 5 && fuelPerMj > 0 && mjPerTick > 0) {
      val needFuel = Misc.clamp((10 * fuelPerTick).ceil, 0F, fuel.getFluidAmount.toFloat).floor.toInt
      burnTime += needFuel / fuelPerTick
      fuel.drain(needFuel, true)
      fuelPerTickAverage.update(needFuel)
    } else {
      fuelPerTickAverage.update(0)
    }

    if (burnTime > 1 && power.capacity - power.stored > mjPerTick) {
      burnTime -= 1
      power.stored += mjPerTick
      outputAverage.update(mjPerTick.toDouble)
      lastChange = worldObj.getTotalWorldTime
    } else {
      outputAverage.update(0)
    }
  }

  serverTick.listen(doUpdate)

  override def openGui(player: EntityPlayer) = player.openGui(Generators, cfg.guiId, worldObj, xCoord, yCoord, zCoord)

  def inputFluid(resource: FluidStack, doFill: Boolean): Int =
    if (canInputFluid(resource.getFluid)) fuel.fill(resource, doFill) else 0

  def canInputFluid(fluid: Fluid) = TurbineFuel.isValidFuel(fluid)
  def getTankInfo = Array(fuel.getInfo)

  def extract(v: Float, simulate: Boolean) = power.extract(v, simulate)

  def onModulesChanged() {
    fuel.setCapacity(getNumOfModules("FuelTank") * Modules.FuelTank.capacity + cfg.internalFuelCapacity)
    power.capacity = getNumOfModules("PowerCapacitor") * Modules.PowerCapacitor.capacity + cfg.internalPowerCapacity
    numTurbines := getNumOfModules("Turbine")
    mjPerTick := numTurbines * cfg.mjPerTickPerTurbine
  }
}
