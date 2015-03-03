/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.sensor.data

import net.bdew.generators.sensor.Icons
import net.bdew.lib.power.DataSlotPower
import net.bdew.lib.sensors.{SensorParameter, SimpleSensor}
import net.minecraft.tileentity.TileEntity

case class SensorPower(uid: String, iconName: String, accessor: TileEntity => Option[DataSlotPower]) extends SimpleSensor with Icons.Loader {
  override val parameters = Vector(
    ParamFullness.empty,
    ParamFullness.nonEmpty,
    ParamFullness.gt25,
    ParamFullness.gt50,
    ParamFullness.gt75,
    ParamFullness.nonFull,
    ParamFullness.full
  )
  override def isActive(param: SensorParameter, te: TileEntity) = param match {
    case x: ParameterCompareFullness =>
      accessor(te) exists { ds => x.test(ds.stored / ds.capacity) }
    case _ => false
  }
}
