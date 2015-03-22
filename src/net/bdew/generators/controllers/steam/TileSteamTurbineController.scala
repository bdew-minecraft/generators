/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.controllers.steam

import net.bdew.generators.config.Modules
import net.bdew.generators.controllers.PoweredController
import net.bdew.generators.sensor.Sensors
import net.bdew.generators.{Generators, GeneratorsResourceProvider}
import net.bdew.lib.data.base.UpdateKind
import net.bdew.lib.data.{DataSlotDouble, DataSlotInt, DataSlotMovingAverage, DataSlotTank}
import net.bdew.lib.multiblock.interact.{CIFluidInput, CIOutputFaces, CIPowerProducer}
import net.bdew.lib.multiblock.tile.TileControllerGui
import net.bdew.lib.power.DataSlotPower
import net.bdew.lib.sensors.multiblock.CIRedstoneSensors
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fluids.{Fluid, FluidStack}

class TileSteamTurbineController extends TileControllerGui with PoweredController with CIFluidInput with CIOutputFaces with CIPowerProducer with CIRedstoneSensors {
  val cfg = MachineSteamTurbine

  val resources = GeneratorsResourceProvider

  val steam = new DataSlotTank("steam", this, cfg.internalSteamCapacity)
  val power = new DataSlotPower("power", this)
  val speed = new DataSlotDouble("speed", this).setUpdate(UpdateKind.GUI, UpdateKind.SAVE)
  val numTurbines = new DataSlotInt("turbines", this).setUpdate(UpdateKind.GUI)

  val outputAverage = new DataSlotMovingAverage("outputAverage", this, 20)
  val steamAverage = new DataSlotMovingAverage("steamAverage", this, 20)

  lazy val maxOutputs = 6

  override val sensorTypes = Sensors.steamTurbineSensors

  def doUpdate() {
    if (speed > 1 && power.stored < power.capacity) {
      val canGenerate = Math.min(speed / cfg.effectiveRPM, 1) * numTurbines * cfg.mjPerTickPerTurbine
      val injected = Math.min(canGenerate, power.capacity - power.stored)
      power.stored += injected.toFloat
      outputAverage.update(injected)
      speed -= cfg.maxRPM * cfg.spinDownMultiplier * (injected / numTurbines / cfg.mjPerTickPerTurbine)
      if (speed < 1)
        speed := 0
      lastChange = worldObj.getTotalWorldTime
    } else outputAverage.update(0)

    if (steam.getFluidAmount > 0) {
      val steamPerTick = cfg.steamPerTickPerTurbine * numTurbines
      val canUseSteam = Math.min(steam.getFluidAmount, steamPerTick)
      steam.drain(canUseSteam.ceil.toInt, true)
      if ((canUseSteam / steamPerTick) * cfg.maxRPM > speed)
        speed += ((canUseSteam / steamPerTick) * cfg.maxRPM - speed) * cfg.spinUpMultiplier
      steamAverage.update(canUseSteam)
    } else steamAverage.update(0)

    if (speed > cfg.maxRPM)
      speed := cfg.maxRPM
  }

  serverTick.listen(doUpdate)

  override def openGui(player: EntityPlayer) = player.openGui(Generators, cfg.guiId, worldObj, xCoord, yCoord, zCoord)

  def inputFluid(resource: FluidStack, doFill: Boolean): Int =
    if (canInputFluid(resource.getFluid)) steam.fill(resource, doFill) else 0

  def canInputFluid(fluid: Fluid) = fluid != null && fluid.getName == "steam"
  def getTankInfo = Array(steam.getInfo)

  def extract(v: Float, simulate: Boolean) = power.extract(v, simulate)

  def onModulesChanged() {
    power.capacity = getNumOfModules("PowerCapacitor") * Modules.PowerCapacitor.capacity + cfg.internalPowerCapacity
    numTurbines := getNumOfModules("Turbine")
  }
}
