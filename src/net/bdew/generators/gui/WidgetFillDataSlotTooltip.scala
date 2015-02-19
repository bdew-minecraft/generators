/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.gui

import net.bdew.lib.data.DataSlotDouble
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.WidgetFillDataSlot

import scala.collection.mutable

class WidgetFillDataSlotTooltip(rect: Rect, texture: Texture, dir: Direction.Direction, dSlot: DataSlotDouble, maxVal: Double, toolTip: => Iterable[String]) extends WidgetFillDataSlot(rect, texture, dir, dSlot, maxVal) {
  override def handleTooltip(p: Point, tip: mutable.MutableList[String]) = tip ++= toolTip
}
