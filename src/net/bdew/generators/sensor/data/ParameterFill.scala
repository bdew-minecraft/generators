/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.sensor.data

import net.bdew.generators.sensor.{Icons, Sensors}

case class ParameterFill(uid: String, iconName: String, test: (Double, Double) => Boolean) extends Sensors.SimpleParameter with Icons.Loader

object ParameterFill {
  val empty = ParameterFill("fill.empty", "fillEmpty", (n, c) => n <= 0)
  val nonEmpty = ParameterFill("fill.not.empty", "fillNotEmpty", (n, c) => n > 0)
  val gt5 = ParameterFill("fill.gt5", "fill5", _ / _ >= 0.05D)
  val gt25 = ParameterFill("fill.gt25", "fill25", _ / _ >= 0.25D)
  val gt50 = ParameterFill("fill.gt50", "fill50", _ / _ >= 0.50D)
  val gt75 = ParameterFill("fill.gt75", "fill75", _ / _ >= 0.75D)
  val gt95 = ParameterFill("fill.gt95", "fill95", _ / _ >= 0.95D)
  val full = ParameterFill("fill.full", "fillFull", _ >= _)
  val nonFull = ParameterFill("fill.not.full", "fillNotFull", _ < _)
}
