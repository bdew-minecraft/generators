package net.bdew.generators.sensor.data

import net.bdew.generators.sensor.{CastSensor, Icons, Sensors}
import net.bdew.lib.Text
import net.bdew.lib.sensors.GenericSensorParameter
import net.minecraft.network.chat.Component
import net.minecraft.world.level.block.entity.BlockEntity

import scala.reflect.ClassTag

case class ParameterNumber[N: Numeric](uid: String, iconName: String, test: N => Boolean, format: String) extends Sensors.SimpleParameter with Icons.Loader

case class SensorNumber[T: ClassTag, N: Numeric](uid: String, iconName: String, accessor: T => N, parameters: Vector[ParameterNumber[N]]) extends CastSensor[T] with Icons.Loader {
  override def getResultTyped(param: GenericSensorParameter, te: T): Boolean = (param, te) match {
    case (x: ParameterNumber[N], y: T) =>
      x.test(accessor(y))
    case _ => false
  }
  override def getParamTooltip(obj: BlockEntity, param: GenericSensorParameter): List[Component] = param match {
    case x: ParameterNumber[_] => List(Text.translate(Sensors.localizationPrefix + ".param." + x.uid, x.format))
    case _ => super.getParamTooltip(obj, param)
  }
}
