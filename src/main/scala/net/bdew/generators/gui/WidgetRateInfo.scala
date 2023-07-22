package net.bdew.generators.gui

import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.Widget
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component

import scala.collection.mutable.ArrayBuffer

class WidgetRateInfo(val rect: Rect, icon: => Texture, iconColor: => Color, text: => Component, tooltip: => Component) extends Widget {
  final val iconRect = Rect(1, 1, 8, 8)

  override def handleTooltip(p: Point, tip: ArrayBuffer[Component]): Unit = {
    if (iconRect.contains(p - rect.origin) && tooltip != null) tip += tooltip
  }

  override def draw(graphics: GuiGraphics, mouse: Point, partial: Float): Unit = {
    parent.drawTexture(graphics, iconRect + rect.origin, icon, iconColor)
    parent.drawText(graphics, text, rect.origin + (12, 1), Color.darkGray, false)
  }
}
