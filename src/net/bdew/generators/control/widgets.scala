/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.control

import net.bdew.lib.Misc
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.Widget
import net.minecraft.init.Blocks

import scala.collection.mutable

class WidgetControlMode(val p: Point, state: => Boolean) extends Widget {
  lazy val rsOn = new IconWrapper(Texture.BLOCKS, Blocks.redstone_torch.getIcon(0, 0))
  lazy val rsOff = new IconWrapper(Texture.BLOCKS, Blocks.unlit_redstone_torch.getIcon(0, 0))

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
