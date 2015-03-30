/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.sensor.data

import net.bdew.generators.sensor.{CastSensor, Icons, Sensors}
import net.bdew.lib.Misc
import net.bdew.lib.sensors.GenericSensorParameter
import net.minecraft.tileentity.TileEntity

import scala.reflect.ClassTag

case class ParameterNumber[N: Numeric](uid: String, iconName: String, test: N => Boolean, format: String) extends Sensors.SimpleParameter with Icons.Loader

case class SensorNumber[T: ClassTag, N: Numeric](uid: String, iconName: String, accessor: T => N, parameters: Vector[ParameterNumber[N]]) extends CastSensor[T] with Icons.Loader {
  override def getResultTyped(param: GenericSensorParameter, te: T) = (param, te) match {
    case (x: ParameterNumber[N], y: T) =>
      x.test(accessor(y))
    case _ => false
  }
  override def getParamTooltip(obj: TileEntity, param: GenericSensorParameter): List[String] = param match {
    case x: ParameterNumber[_] => List(Misc.toLocalF(Sensors.localizationPrefix + ".param." + x.uid, x.format))
    case _ => super.getParamTooltip(obj, param)
  }

}
