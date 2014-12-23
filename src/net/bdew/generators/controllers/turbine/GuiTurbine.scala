/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.controllers.turbine

import net.bdew.generators.gui.{GuiOutputConfig, GuiOutputFaces}
import net.bdew.generators.{Generators, Textures}
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.{WidgetButtonIcon, WidgetFluidGauge, WidgetLabel}
import net.bdew.lib.multiblock.gui.WidgetInfo
import net.bdew.lib.power.WidgetPowerGauge
import net.bdew.lib.{Client, DecFormat, Misc}
import net.minecraft.entity.player.EntityPlayer

class GuiTurbine(val te: TileTurbineController, player: EntityPlayer) extends BaseScreen(new ContainerTurbine(te, player), 176, 175) with GuiOutputFaces {
  val background = Texture(Generators.modId, "textures/gui/turbine.png", rect)
  override def initGui() {
    super.initGui()
    widgets.add(new WidgetPowerGauge(new Rect(61, 19, 9, 58), Textures.powerFill, te.power))
    widgets.add(new WidgetFluidGauge(new Rect(9, 19, 9, 58), Textures.tankOverlay, te.fuel))
    widgets.add(new WidgetButtonIcon(Point(153, 18), openCfg, Textures.Button16.base, Textures.Button16.hover) {
      icon = Textures.Button16.wrench
      hover = Misc.toLocal("advgenerators.gui.output.title")
    })
    widgets.add(new WidgetLabel(Misc.toLocal("advgenerators.gui.turbine.title"), 8, 6, Color.darkGray))
    widgets.add(new WidgetLabel(Misc.toLocal("container.inventory"), 8, this.ySize - 96 + 3, Color.darkGray))
    widgets.add(new WidgetInfo(Rect(75, 21, 59, 10), Textures.Icons.turbine, te.numTurbines.value.toString, Misc.toLocal("advgenerators.label.turbine.turbines")))
    widgets.add(new WidgetInfo(Rect(75, 32, 59, 10), Textures.Icons.peak, DecFormat.round(te.mjPerTick) + " MJ/t", Misc.toLocal("advgenerators.label.turbine.maxprod")))
    widgets.add(new WidgetInfo(Rect(75, 43, 59, 10), Textures.Icons.fluid, DecFormat.round(te.fuelPerTick) + " mB/t", Misc.toLocal("advgenerators.label.turbine.fuel")))
    widgets.add(new WidgetInfo(Rect(75, 54, 59, 10), Textures.Icons.power, DecFormat.dec2(te.mjPerTickAvg) + " MJ/t", Misc.toLocal("advgenerators.label.turbine.prod")))
  }

  def openCfg(b: WidgetButtonIcon) {
    Client.minecraft.displayGuiScreen(new GuiOutputConfig(this))
  }
}
