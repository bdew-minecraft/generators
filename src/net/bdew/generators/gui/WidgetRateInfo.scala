package net.bdew.generators.gui

import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.Widget

import scala.collection.mutable

class WidgetRateInfo(val rect: Rect, icon: => Texture, iconColor: => Color, text: => String, tooltip: => String) extends Widget {
  final val iconRect = Rect(1, 1, 8, 8)

  override def handleTooltip(p: Point, tip: mutable.MutableList[String]) {
    if (iconRect.contains(p) && tooltip != null) tip += tooltip
  }

  override def draw(mouse: Point) {
    parent.drawTexture(iconRect + rect.origin, icon, iconColor)
    parent.drawText(text, rect.origin +(12, 1), Color.darkGray, false)
  }
}
