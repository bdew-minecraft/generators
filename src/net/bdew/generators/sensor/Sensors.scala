/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.sensor

import net.bdew.generators.Generators
import net.bdew.generators.controllers.PoweredController
import net.bdew.generators.controllers.steam.TileSteamTurbineController
import net.bdew.generators.controllers.turbine.TileTurbineController
import net.bdew.generators.sensor.data.{SensorPower, SensorTank}
import net.bdew.lib.data.DataSlotTankBase
import net.bdew.lib.power.DataSlotPower
import net.bdew.lib.sensors.SensorRegistry
import net.minecraft.tileentity.TileEntity

import scala.reflect.ClassTag

object Sensors {
  def accessHelper[T: ClassTag, R](ds: T => R): TileEntity => Option[R] = { t =>
    if (implicitly[ClassTag[T]].runtimeClass.isInstance(t))
      Some(ds(t.asInstanceOf[T]))
    else
      None
  }

  SensorRegistry.register(SensorNull)
  val powerSensor = SensorRegistry.register(SensorPower("generators.power", "power",
    accessHelper[PoweredController, DataSlotPower](_.power)))

  val fuelTurbineSensors = List(
    SensorNull,
    powerSensor,
    SensorRegistry.register(SensorTank("generators.turbine.fuel", "fuelTank",
      accessHelper[TileTurbineController, DataSlotTankBase](_.fuel)))
  )

  val steamTurbineSensors = List(
    SensorNull,
    powerSensor,
    SensorRegistry.register(SensorTank("generators.turbine.steam", "steamTank",
      accessHelper[TileSteamTurbineController, DataSlotTankBase](_.steam)))
  )

  def load(): Unit = {
    Generators.logDebug("Sensors loaded")
  }
}
