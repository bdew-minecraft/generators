/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.sensor

import net.bdew.lib.sensors.{SensorParameter, SimpleSensor}
import net.minecraft.tileentity.TileEntity

object ParameterNull extends SensorParameter with Icons.Loader {
  override val uid = "generators.disabled"
  override def iconName = "disabled"
}

object SensorNull extends SimpleSensor with Icons.Loader {
  override def uid = "generators.disabled"
  override def iconName = "disabled"
  override val parameters = Vector(ParameterNull)
  override def isActive(param: SensorParameter, te: TileEntity) = false
}
