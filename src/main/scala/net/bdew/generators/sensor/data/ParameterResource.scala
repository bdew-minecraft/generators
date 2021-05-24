package net.bdew.generators.sensor.data

import net.bdew.generators.sensor.Sensors
import net.bdew.lib.resource.{DataSlotResource, FluidResource, ItemResource}

case class ParameterResource(uid: String, test: DataSlotResource => Boolean) extends Sensors.SensorParameter

object ParameterResource {
  val solid: ParameterResource = ParameterResource("resource.solid", _.resource exists (_.kind.isInstanceOf[ItemResource]))
  val fluid: ParameterResource = ParameterResource("resource.fluid", _.resource exists (_.kind.isInstanceOf[FluidResource]))
}
