/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.sensor.data

import net.bdew.generators.sensor.Icons
import net.bdew.lib.sensors.SensorParameter

case class ParameterCompareFullness(uid: String, iconName: String, test: (Double) => Boolean) extends SensorParameter with Icons.Loader

object ParamFullness {
  val empty = ParameterCompareFullness("generators.fullness.empty", "fillEmpty", _ <= 0.000001D)
  val nonEmpty = ParameterCompareFullness("generators.fullness.not.empty", "fillNotEmpty", _ > 0.000001D)
  val gt25 = ParameterCompareFullness("generators.fullness.gt25", "fill25", _ >= 0.25D)
  val gt50 = ParameterCompareFullness("generators.fullness.gt50", "fill50", _ >= 0.50D)
  val gt75 = ParameterCompareFullness("generators.fullness.gt75", "fill75", _ >= 0.75D)
  val full = ParameterCompareFullness("generators.fullness.full", "fillFull", _ >= 0.9999999D)
  val nonFull = ParameterCompareFullness("generators.fullness.not.full", "fillNotFull", _ < 0.9999999D)
}
