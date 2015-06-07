/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.gui

import net.bdew.lib.Misc
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.Widget
import net.bdew.lib.resource.DataSlotResource
import org.lwjgl.opengl.GL11

import scala.collection.mutable

class WidgetResourceGauge(val rect: Rect, overlay: Texture, dSlot: DataSlotResource) extends Widget {
  override def handleTooltip(p: Point, tip: mutable.MutableList[String]) {
    tip ++= (dSlot.resource map { res =>
      List(res.kind.getLocalizedName, res.kind.getFormattedString(res.amount, dSlot.getEffectiveCapacity))
    } getOrElse List(Misc.toLocal("bdlib.label.empty")))
  }

  override def draw(mouse: Point) {
    GL11.glEnable(GL11.GL_BLEND)
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

    dSlot.resource foreach { resource =>
      val color = resource.kind.getColor
      val icon = resource.kind.getTexture
      var fillHeight = if (dSlot.getEffectiveCapacity > 0) rect.h * resource.amount / dSlot.getEffectiveCapacity else 0
      var yStart: Int = 0

      while (fillHeight > 0) {
        if (fillHeight > 16) {
          parent.drawTexture(new Rect(rect.x, rect.y2 - 16 - yStart, rect.w, 16), icon, color)
          fillHeight -= 16
        } else {
          parent.drawTextureInterpolate(new Rect(rect.x, rect.y2 - 16 - yStart, rect.w, 16), icon, 0, 1 - fillHeight.toFloat / 16, 1, 1, color)
          fillHeight = 0
        }
        yStart = yStart + 16
      }
    }

    GL11.glColor3d(1, 1, 1)
    GL11.glDisable(GL11.GL_BLEND)

    if (overlay != null)
      parent.drawTexture(rect, overlay)
  }
}