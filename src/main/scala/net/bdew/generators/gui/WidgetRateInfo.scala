package net.bdew.generators.gui

import com.mojang.blaze3d.vertex.PoseStack
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.Widget
import net.minecraft.network.chat.Component

import scala.collection.mutable.ArrayBuffer

class WidgetRateInfo(val rect: Rect, icon: => Texture, iconColor: => Color, text: => Component, tooltip: => Component) extends Widget {
  final val iconRect = Rect(1, 1, 8, 8)

  override def handleTooltip(p: Point, tip: ArrayBuffer[Component]): Unit = {
    if (iconRect.contains(p - rect.origin) && tooltip != null) tip += tooltip
  }

  override def draw(m: PoseStack, mouse: Point, partial: Float): Unit = {
    parent.drawTexture(m, iconRect + rect.origin, icon, iconColor)
    parent.drawText(m, text, rect.origin + (12, 1), Color.darkGray, false)
  }
}
