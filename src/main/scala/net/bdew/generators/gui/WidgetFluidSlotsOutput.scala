package net.bdew.generators.gui

import net.bdew.lib.Text
import net.bdew.lib.gui.widgets.WidgetDynLabel
import net.bdew.lib.gui.{Color, Point}
import net.bdew.lib.multiblock.data.OutputConfigFluidSlots
import net.bdew.lib.multiblock.gui.WidgetRSConfig
import net.bdew.lib.multiblock.interact.CIOutputFaces

class WidgetFluidSlotsOutput(te: CIOutputFaces, output: Int) extends WidgetOutputDisplay {
  def cfg: OutputConfigFluidSlots = te.outputConfig(output).asInstanceOf[OutputConfigFluidSlots]
  add(new WidgetDynLabel(Text.fluidPerTick(cfg.avg), 1, 5, Color.darkGray))
  add(new WidgetSlotConfig(te, output, Point(55, 1)))
  add(new WidgetRSConfig(te, output, Point(73, 1)))
}
