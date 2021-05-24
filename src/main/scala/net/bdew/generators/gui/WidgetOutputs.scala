package net.bdew.generators.gui

import net.bdew.lib.gui.widgets.{BaseWidget, WidgetMultiPane, WidgetSubContainer}
import net.bdew.lib.gui.{Rect, _}
import net.bdew.lib.multiblock.data.{OutputConfigFluidSlots, OutputConfigItems, OutputConfigPower}
import net.bdew.lib.multiblock.gui.WidgetOutputIcon
import net.bdew.lib.multiblock.interact.CIOutputFaces

class WidgetOutputs(p: Point, te: CIOutputFaces, rows: Int) extends WidgetSubContainer(new Rect(p, 108, 19f * rows)) {
  for (i <- 0 until rows)
    add(new WidgetOutputRow(Point(0, 19 * i), te, i))
}

class WidgetOutputDisplay extends WidgetSubContainer(Rect(20, 0, 88, 18))

class WidgetOutputRow(p: Point, te: CIOutputFaces, output: Int) extends WidgetMultiPane(new Rect(p, 108, 18)) {
  add(new WidgetOutputIcon(Point(1, 1), te, output))
  val emptyPane: WidgetSubContainer = addPane(new WidgetSubContainer(rect))
  val powerPane: WidgetPowerOutput = addPane(new WidgetPowerOutput(te, output))
  val fluidSlotsPane: WidgetFluidSlotsOutput = addPane(new WidgetFluidSlotsOutput(te, output))
  val itemPane: WidgetItemsOutput = addPane(new WidgetItemsOutput(te, output))

  def getActivePane: BaseWidget =
    te.outputConfig.get(output) match {
      case Some(_: OutputConfigPower) => powerPane
      case Some(_: OutputConfigFluidSlots) => fluidSlotsPane
      case Some(_: OutputConfigItems) => itemPane
      case _ => emptyPane
    }
}