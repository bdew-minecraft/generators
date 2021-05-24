package net.bdew.generators.gui

import net.bdew.lib.gui.Point
import net.bdew.lib.multiblock.data.OutputConfigItems
import net.bdew.lib.multiblock.gui.WidgetRSConfig
import net.bdew.lib.multiblock.interact.CIOutputFaces

class WidgetItemsOutput(te: CIOutputFaces, output: Int) extends WidgetOutputDisplay {
  def cfg: OutputConfigItems = te.outputConfig(output).asInstanceOf[OutputConfigItems]
  add(new WidgetRSConfig(te, output, Point(73, 1)))
}
