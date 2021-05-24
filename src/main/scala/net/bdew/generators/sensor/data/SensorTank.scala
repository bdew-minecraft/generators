package net.bdew.generators.sensor.data

import net.bdew.generators.sensor.{CastSensor, Icons}
import net.bdew.lib.data.DataSlotTankBase
import net.bdew.lib.sensors.GenericSensorParameter

import scala.reflect.ClassTag

case class SensorTank[T: ClassTag](uid: String, iconName: String, accessor: T => DataSlotTankBase) extends CastSensor[T] with Icons.Loader {
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
  override def getResultTyped(param: GenericSensorParameter, te: T): Boolean = (param, te) match {
    case (x: ParameterFill, y: T) =>
      val ds = accessor(y)
      x.test(ds.getFluidAmount, ds.getCapacity)
    case _ => false
  }
}
