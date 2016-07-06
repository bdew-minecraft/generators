/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.control

import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.Widget
import net.bdew.lib.{Client, Misc}

import scala.collection.mutable

class WidgetControlMode(val p: Point, state: => Boolean) extends Widget {
  lazy val rsOn = Texture(Texture.BLOCKS, Client.textureMapBlocks.getAtlasSprite("minecraft:blocks/redstone_torch_on"))
  lazy val rsOff = Texture(Texture.BLOCKS, Client.textureMapBlocks.getAtlasSprite("minecraft:blocks/redstone_torch_off"))

  override val rect = new Rect(p, 16, 16)

  override def handleTooltip(p: Point, tip: mutable.MutableList[String]) =
    if (state)
      tip += Misc.toLocal("advgenerators.control.mode.high")
    else
      tip += Misc.toLocal("advgenerators.control.mode.low")

  override def draw(mouse: Point) =
    if (state)
      parent.drawTexture(rect, rsOn)
    else
      parent.drawTexture(rect, rsOff)
}

class WidgetControlAction(val p: Point, action: => ControlAction) extends Widget {
  override val rect = new Rect(p, 16, 16)

  override def handleTooltip(p: Point, tip: mutable.MutableList[String]) =
    tip += Misc.toLocal("advgenerators.control.action." + action.uid)

  override def draw(mouse: Point) =
    parent.drawTexture(rect, action.texture)
}
