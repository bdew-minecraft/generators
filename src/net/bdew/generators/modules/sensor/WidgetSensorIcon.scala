/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.sensor

import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.Widget

import scala.collection.mutable

class WidgetSensorIcon(val p: Point, icon: => Texture, iconColor: => Color, tooltip: => String) extends Widget {
  override val rect = new Rect(p, 16, 16)
  override def handleTooltip(p: Point, tip: mutable.MutableList[String]) = tip += tooltip
  override def draw(mouse: Point) = parent.drawTexture(rect, icon, iconColor)
}
