/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.sensor

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.bdew.generators.controllers.steam.TileSteamTurbineController
import net.bdew.generators.controllers.turbine.TileTurbineController
import net.bdew.generators.sensor.data.{SensorPower, SensorTank}
import net.bdew.lib.sensors.SensorSystem
import net.minecraft.tileentity.TileEntity

object Sensors extends SensorSystem[TileEntity, Boolean](false) {
  @SideOnly(Side.CLIENT)
  override def disabledTexture = Icons.disabled
  override def localizationPrefix = "advgenerators.sensor"

  val fuelTurbineSensors = List(
    DisabledSensor,
    SensorPower,
    SensorTank[TileTurbineController]("turbine.fuel", "fuelTank", _.fuel)
  )

  val steamTurbineSensors = List(
    DisabledSensor,
    SensorPower,
    SensorTank[TileSteamTurbineController]("turbine.steam", "steamTank", _.steam)
  )
}
