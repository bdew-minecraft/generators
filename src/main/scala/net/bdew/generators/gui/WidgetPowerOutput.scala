package net.bdew.generators.gui

import net.bdew.lib.Text
import net.bdew.lib.gui.widgets.WidgetDynLabel
import net.bdew.lib.gui.{Color, Point}
import net.bdew.lib.multiblock.data._
import net.bdew.lib.multiblock.gui.WidgetRSConfig
import net.bdew.lib.multiblock.interact.CIOutputFaces

class WidgetPowerOutput(te: CIOutputFaces, output: Int) extends WidgetOutputDisplay {
  def cfg: OutputConfigPower = te.outputConfig(output).asInstanceOf[OutputConfigPower]
  add(new WidgetDynLabel(Text.perTick(cfg.avg, cfg.unit), 1, 5, Color.darkGray))
  add(new WidgetRSConfig(te, output, Point(73, 1)))
}
