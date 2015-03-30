/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.sensor.data

import net.bdew.generators.sensor.Sensors
import net.bdew.lib.resource.{DataSlotResource, FluidResource, ItemResource}

case class ParameterResource(uid: String, test: DataSlotResource => Boolean) extends Sensors.SensorParameter

object ParameterResource {
  val solid = ParameterResource("resource.solid", _.resource exists (_.kind.isInstanceOf[ItemResource]))
  val fluid = ParameterResource("resource.fluid", _.resource exists (_.kind.isInstanceOf[FluidResource]))
}
