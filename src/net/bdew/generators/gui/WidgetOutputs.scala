/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.gui

import net.bdew.lib.gui.widgets.{WidgetMultiPane, WidgetSubContainer}
import net.bdew.lib.gui.{Rect, _}
import net.bdew.lib.multiblock.data.{OutputConfigFluidSlots, OutputConfigItems, OutputConfigPower}
import net.bdew.lib.multiblock.gui.WidgetOutputIcon
import net.bdew.lib.multiblock.interact.CIOutputFaces

class WidgetOutputs(p: Point, te: CIOutputFaces, rows: Int) extends WidgetSubContainer(new Rect(p, 108, 19 * rows)) {
  for (i <- 0 until rows)
    add(new WidgetOutputRow(Point(0, 19 * i), te, i))
}

class WidgetOutputDisplay extends WidgetSubContainer(Rect(20, 0, 88, 18))

class WidgetOutputRow(p: Point, te: CIOutputFaces, output: Int) extends WidgetMultiPane(new Rect(p, 108, 18)) {
  add(new WidgetOutputIcon(Point(1, 1), te, output))
  val emptyPane = addPane(new WidgetSubContainer(rect))
  val powerPane = addPane(new WidgetPowerOutput(te, output))
  val fluidSlotsPane = addPane(new WidgetFluidSlotsOutput(te, output))
  val itemPane = addPane(new WidgetItemsOutput(te, output))

  def getActivePane =
    te.outputConfig.get(output) match {
      case Some(x: OutputConfigPower) => powerPane
      case Some(x: OutputConfigFluidSlots) => fluidSlotsPane
      case Some(x: OutputConfigItems) => itemPane
      case _ => emptyPane
    }
}