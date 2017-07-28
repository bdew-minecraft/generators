/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.gui

import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.Widget

import scala.collection.mutable

class WidgetRateInfo(val rect: Rect, icon: => Texture, iconColor: => Color, text: => String, tooltip: => String) extends Widget {
  final val iconRect = Rect(1, 1, 8, 8)

  override def handleTooltip(p: Point, tip: mutable.MutableList[String]) {
    if (iconRect.contains(p) && tooltip != null) tip += tooltip
  }

  override def draw(mouse: Point, partial: Float) {
    parent.drawTexture(iconRect + rect.origin, icon, iconColor)
    parent.drawText(text, rect.origin +(12, 1), Color.darkGray, false)
  }
}
