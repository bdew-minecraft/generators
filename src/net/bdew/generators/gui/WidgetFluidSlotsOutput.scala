/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.gui

import net.bdew.lib.DecFormat
import net.bdew.lib.gui.widgets.WidgetDynLabel
import net.bdew.lib.gui.{Color, Point}
import net.bdew.lib.multiblock.data.OutputConfigFluidSlots
import net.bdew.lib.multiblock.gui.WidgetRSConfig
import net.bdew.lib.multiblock.interact.CIOutputFaces

class WidgetFluidSlotsOutput(te: CIOutputFaces, output: Int) extends WidgetOutputDisplay {
  def cfg = te.outputConfig(output).asInstanceOf[OutputConfigFluidSlots]
  add(new WidgetDynLabel("%s mB/t".format(DecFormat.round(cfg.avg)), 1, 5, Color.darkGray))
  add(new WidgetSlotConfig(te, output, Point(55, 1)))
  add(new WidgetRSConfig(te, output, Point(73, 1)))
}
