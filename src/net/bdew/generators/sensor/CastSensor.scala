/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.sensor

import net.bdew.lib.sensors.GenericSensorParameter
import net.minecraft.tileentity.TileEntity

import scala.reflect.ClassTag

abstract class CastSensor[T: ClassTag] extends Sensors.SimpleSensor {
  val teClass = implicitly[ClassTag[T]].runtimeClass

  def getResultTyped(param: GenericSensorParameter, te: T): Boolean

  override def getResult(param: GenericSensorParameter, te: TileEntity) =
    if (teClass.isInstance(te)) getResultTyped(param, te.asInstanceOf[T]) else false
}
