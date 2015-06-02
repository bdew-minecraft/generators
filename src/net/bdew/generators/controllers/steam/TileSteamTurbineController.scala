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
import net.bdew.generators.modules.turbine.BlockTurbineBase
import net.bdew.generators.sensor.Sensors
import net.bdew.generators.{Generators, GeneratorsResourceProvider}
import net.bdew.lib.Misc
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

  val inertiaMultiplier = new DataSlotDouble("inertiaMultiplier", this).setUpdate(UpdateKind.SAVE)
  val maxMJPerTick = new DataSlotDouble("maxMJPerTick", this).setUpdate(UpdateKind.SAVE, UpdateKind.GUI)

  val outputAverage = new DataSlotMovingAverage("outputAverage", this, 20)
  val steamAverage = new DataSlotMovingAverage("steamAverage", this, 20)

  def canGeneratePower = true
  def canUseSteam = true

  lazy val maxOutputs = 6

  override val redstoneSensorsType = Sensors.steamTurbineSensors
  override val redstoneSensorSystem = Sensors

  def doUpdate() {
    if (maxMJPerTick > 0) {
      val maxSpeedDelta = cfg.maxRPM * cfg.inertiaFunctionMultiplier * Math.exp(cfg.inertiaFunctionExponent * speed / cfg.maxRPM) * inertiaMultiplier

      speed -= maxSpeedDelta * cfg.baseDragMultiplier

      if (canGeneratePower && speed > 1) {
        if (power.stored < power.capacity) {
          val injected = Math.min(speed / cfg.maxRPM * maxMJPerTick, power.capacity - power.stored)
          power.stored += injected.toFloat
          outputAverage.update(injected)
        } else outputAverage.update(0)
        speed -= maxSpeedDelta * cfg.coilDragMultiplier
      } else outputAverage.update(0)

      if (canUseSteam && steam.getFluidAmount > 0) {
        val maxSteamPerTick = maxMJPerTick / cfg.steamEnergyDensity
        val useSteam = Math.min(steam.getFluidAmount, maxSteamPerTick).ceil.toInt
        steam.drain(useSteam, true)
        steamAverage.update(useSteam)
        speed := Misc.clamp((useSteam / maxSteamPerTick) * cfg.maxRPM, speed.value, speed.value + (maxSpeedDelta * cfg.spinUpMultiplier))
      } else steamAverage.update(0)

      if (speed < 1)
        speed := 0

    } else speed := 0
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

    val turbines = modules.toList.flatMap(_.getBlock[BlockTurbineBase[_]](getWorldObj))
    maxMJPerTick := turbines.map(_.maxMJPerTick).sum
    inertiaMultiplier := turbines.map(_.inertiaMultiplier).sum / turbines.size
    numTurbines := turbines.size
  }

}
