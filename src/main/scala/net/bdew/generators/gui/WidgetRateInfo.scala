package net.bdew.generators.gui

import com.mojang.blaze3d.matrix.MatrixStack
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.Widget
import net.minecraft.util.text.ITextComponent

import scala.collection.mutable.ArrayBuffer

class WidgetRateInfo(val rect: Rect, icon: => Texture, iconColor: => Color, text: => ITextComponent, tooltip: => ITextComponent) extends Widget {
  final val iconRect = Rect(1, 1, 8, 8)

  override def handleTooltip(p: Point, tip: ArrayBuffer[ITextComponent]): Unit = {
    if (iconRect.contains(p - rect.origin) && tooltip != null) tip += tooltip
  }

  override def draw(m: MatrixStack, mouse: Point, partial: Float): Unit = {
    parent.drawTexture(m, iconRect + rect.origin, icon, iconColor)
    parent.drawText(m, text, rect.origin + (12, 1), Color.darkGray, false)
  }
}
