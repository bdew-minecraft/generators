package net.bdew.generators.gui

import com.mojang.blaze3d.matrix.MatrixStack
import net.bdew.lib.Text
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.Widget
import net.bdew.lib.resource.DataSlotResource
import net.minecraft.util.text.ITextComponent

import scala.collection.mutable.ArrayBuffer

class WidgetResourceGauge(val rect: Rect, overlay: Texture, dSlot: DataSlotResource) extends Widget {
  override def handleTooltip(p: Point, tip: ArrayBuffer[ITextComponent]): Unit = {
    tip ++= (dSlot.resource map { res =>
      List(res.kind.getName, res.kind.getFormattedString(res.amount, dSlot.getEffectiveCapacity))
    } getOrElse List(Text.translate("bdlib.label.empty")))
  }

  override def draw(m: MatrixStack, mouse: Point, partial: Float): Unit = {
    dSlot.resource foreach { resource =>
      val color = resource.kind.getColor
      val icon = resource.kind.getTexture
      val fillHeight = if (dSlot.getEffectiveCapacity > 0) rect.h * resource.amount / dSlot.getEffectiveCapacity else 0
      parent.drawTextureTiled(m, Rect(rect.x, rect.y + rect.h - fillHeight, rect.w, fillHeight), icon, 16, 16, color)
    }

    if (overlay != null)
      parent.drawTexture(m, rect, overlay)
  }
}