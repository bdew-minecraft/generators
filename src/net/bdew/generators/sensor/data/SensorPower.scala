/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.sensor.data

import net.bdew.generators.controllers.PoweredController
import net.bdew.generators.sensor.{CastSensor, Icons}
import net.bdew.lib.sensors.GenericSensorParameter

object SensorPower extends CastSensor[PoweredController] with Icons.Loader {
  override def iconName = "power"
  override def uid = "power"

  override val parameters = Vector(
    ParameterFill.empty,
    ParameterFill.nonEmpty,
    ParameterFill.gt5,
    ParameterFill.gt25,
    ParameterFill.gt50,
    ParameterFill.gt75,
    ParameterFill.gt95,
    ParameterFill.nonFull,
    ParameterFill.full
  )

  override def getResultTyped(param: GenericSensorParameter, te: PoweredController) = param match {
    case x: ParameterFill =>
      x.test(te.power.stored, te.power.capacity)
    case _ => false
  }
}
