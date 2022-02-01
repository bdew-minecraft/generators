package net.bdew.generators.sensor

import net.bdew.lib.sensors.GenericSensorParameter
import net.minecraft.world.level.block.entity.BlockEntity

import scala.reflect.ClassTag

abstract class CastSensor[T: ClassTag] extends Sensors.SimpleSensor {
  val teClass: Class[_] = implicitly[ClassTag[T]].runtimeClass

  def getResultTyped(param: GenericSensorParameter, te: T): Boolean

  override def getResult(param: GenericSensorParameter, te: BlockEntity): Boolean =
    if (teClass.isInstance(te)) getResultTyped(param, te.asInstanceOf[T]) else false
}
