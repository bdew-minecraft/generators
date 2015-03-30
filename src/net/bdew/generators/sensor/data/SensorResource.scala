/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.sensor.data

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.bdew.generators.sensor.{CastSensor, Icons}
import net.bdew.lib.Client
import net.bdew.lib.gui.{Color, DrawTarget, Rect, Texture}
import net.bdew.lib.resource.DataSlotResource
import net.bdew.lib.sensors.GenericSensorParameter
import net.minecraft.client.renderer.entity.RenderItem
import net.minecraft.init.{Blocks, Items}
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import org.lwjgl.opengl.GL11

import scala.reflect.ClassTag

case class SensorResource[T: ClassTag](uid: String, iconName: String, accessor: T => DataSlotResource) extends CastSensor[T] with Icons.Loader {
  override val parameters = Vector(
    ParameterFill.empty,
    ParameterResource.solid,
    ParameterResource.fluid,
    ParameterFill.gt25,
    ParameterFill.gt50,
    ParameterFill.gt75,
    ParameterFill.nonFull,
    ParameterFill.full
  )

  @SideOnly(Side.CLIENT) override
  def drawParameter(rect: Rect, target: DrawTarget, obj: TileEntity, param: GenericSensorParameter): Unit = param match {
    case ParameterResource.solid =>
      GL11.glPushAttrib(GL11.GL_ENABLE_BIT)
      GL11.glEnable(GL11.GL_LIGHTING)
      GL11.glDisable(GL11.GL_DEPTH_TEST)
      RenderItem.getInstance().renderItemAndEffectIntoGUI(target.getFontRenderer, Client.textureManager, new ItemStack(Blocks.cobblestone), rect.origin.x.toInt, rect.origin.y.toInt)
      GL11.glPopAttrib()
    case ParameterResource.fluid =>
      target.drawTexture(rect, Texture(Texture.ITEMS, Items.water_bucket.getIconFromDamage(0)), Color.white)
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
