package net.bdew.generators.sensor.data

import net.bdew.generators.sensor.{CastSensor, Icons}
import net.bdew.lib.multiblock.interact.CIPowerOutput
import net.bdew.lib.sensors.GenericSensorParameter

object SensorPower extends CastSensor[CIPowerOutput] with Icons.Loader {
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

  override def getResultTyped(param: GenericSensorParameter, te: CIPowerOutput): Boolean = param match {
    case x: ParameterFill =>
      x.test(te.powerOutput.getEnergyStored, te.powerOutput.getMaxEnergyStored)
    case _ => false
  }
}
