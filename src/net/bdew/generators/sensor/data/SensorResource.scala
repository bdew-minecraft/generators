/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.sensor.data

import net.bdew.generators.sensor.{CastSensor, Icons}
import net.bdew.lib.Client
import net.bdew.lib.gui.{DrawTarget, Rect}
import net.bdew.lib.resource.DataSlotResource
import net.bdew.lib.sensors.GenericSensorParameter
import net.minecraft.init.{Blocks, Items}
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

import scala.reflect.ClassTag

case class SensorResource[T: ClassTag](uid: String, iconName: String, accessor: T => DataSlotResource) extends CastSensor[T] with Icons.Loader {
  override val parameters = Vector(
    ParameterFill.empty,
    ParameterResource.solid,
    ParameterResource.fluid,
    ParameterFill.gt5,
    ParameterFill.gt25,
    ParameterFill.gt50,
    ParameterFill.gt75,
    ParameterFill.gt95,
    ParameterFill.nonFull,
    ParameterFill.full
  )

  @SideOnly(Side.CLIENT) override
  def drawParameter(rect: Rect, target: DrawTarget, obj: TileEntity, param: GenericSensorParameter): Unit = param match {
    case ParameterResource.solid =>
      Client.minecraft.getRenderItem.renderItemAndEffectIntoGUI(new ItemStack(Blocks.COBBLESTONE), rect.origin.x.toInt, rect.origin.y.toInt)
    case ParameterResource.fluid =>
      Client.minecraft.getRenderItem.renderItemAndEffectIntoGUI(new ItemStack(Items.WATER_BUCKET), rect.origin.x.toInt, rect.origin.y.toInt)
    case _ => super.drawParameter(rect, target, obj, param)
  }

  override def getResultTyped(param: GenericSensorParameter, te: T) = (param, te) match {
    case (x: ParameterResource, y: T) =>
      x.test(accessor(y))

    case (x: ParameterFill, y: T) =>
      val ds = accessor(y)
      ds.resource map { resource =>
        x.test(resource.amount, ds.getEffectiveCapacity)
      } getOrElse {
        x.test(0, ds.capacity)
      }
    case _ => false
  }
}
