/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/generators/master/MMPL-1.0.txt
 */

package net.bdew.generators.blocks.turbineController

import java.text.DecimalFormat

import net.bdew.generators.{Generators, Textures}
import net.bdew.lib.Misc
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.{WidgetFluidGauge, WidgetLabel}
import net.bdew.lib.multiblock.gui.{WidgetInfo, WidgetOutputs}
import net.bdew.lib.power.WidgetPowerGauge
import net.minecraft.entity.player.EntityPlayer

class GuiTurbine(val te: TileTurbineController, player: EntityPlayer) extends BaseScreen(new ContainerTurbine(te, player), 176, 160) {
  val background = Texture(Generators.modId, "textures/gui/turbine.png", rect)
  val dec = new DecimalFormat("0.00")
  val int = new DecimalFormat("0")
  override def initGui() {
    super.initGui()
    widgets.add(new WidgetPowerGauge(new Rect(61, 19, 9, 58), Textures.powerFill, te.power))
    widgets.add(new WidgetFluidGauge(new Rect(9, 19, 9, 58), Textures.tankOverlay, te.fuel))
    widgets.add(new WidgetLabel(Misc.toLocal("advgenerators.gui.turbine.title"), 8, 6, Color.darkgray))
    widgets.add(new WidgetOutputs(Point(76, 18), te, 6))
    widgets.add(new WidgetInfo(Rect(10, 85, 59, 10), Textures.Icons.turbine, te.numTurbines.cval.toString, Misc.toLocal("advgenerators.label.turbine.turbines")))
    widgets.add(new WidgetInfo(Rect(10, 96, 59, 10), Textures.Icons.peak, int.format(te.mjPerTick.cval) + " MJ/t", Misc.toLocal("advgenerators.label.turbine.maxprod")))
    widgets.add(new WidgetInfo(Rect(10, 107, 59, 10), Textures.Icons.fluid, dec.format(te.fuelPerTick.cval) + " MB/t", Misc.toLocal("advgenerators.label.turbine.fuel")))
    widgets.add(new WidgetInfo(Rect(10, 118, 59, 10), Textures.Icons.power, int.format(te.mjPerTickAvg.cval) + " MJ/t", Misc.toLocal("advgenerators.label.turbine.prod")))
  }
}
