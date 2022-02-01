package net.bdew.generators.gui

import net.bdew.lib.data.DataSlotDouble
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.WidgetFillDataSlot
import net.minecraft.network.chat.Component

import scala.collection.mutable.ArrayBuffer

class WidgetFillDataSlotTooltip(rect: Rect, texture: Texture, dir: Direction.Direction, dSlot: DataSlotDouble, maxVal: Double, toolTip: => Iterable[Component]) extends WidgetFillDataSlot(rect, texture, dir, dSlot, maxVal) {
  override def handleTooltip(p: Point, tip: ArrayBuffer[Component]): Unit = tip ++= toolTip
}
