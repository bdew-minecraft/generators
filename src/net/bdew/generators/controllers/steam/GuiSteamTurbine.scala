/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license
 */

package net.bdew.generators.controllers.steam

import net.bdew.generators.config.{Blocks, Tuning}
import net.bdew.generators.gui.{GuiOutputConfig, GuiOutputFaces}
import net.bdew.generators.{Generators, Textures}
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.{WidgetButtonIcon, WidgetFluidGauge, WidgetLabel}
import net.bdew.lib.multiblock.gui.WidgetInfo
import net.bdew.lib.power.WidgetPowerGauge
import net.bdew.lib.{Client, DecFormat, Misc}
import net.minecraft.entity.player.EntityPlayer

class GuiSteamTurbine(val te: TileSteamTurbineController, player: EntityPlayer) extends BaseScreen(new ContainerSteamTurbine(te, player), 176, 175) with GuiOutputFaces {
  val background = Texture(Generators.modId, "textures/gui/turbine.png", rect)
  val ratio = Tuning.getSection("Power").getFloat("RF_MJ_Ratio")

  override def initGui() {
    super.initGui()
    widgets.add(new WidgetPowerGauge(new Rect(61, 19, 9, 58), Textures.powerFill, te.power))
    widgets.add(new WidgetFluidGauge(new Rect(9, 19, 9, 58), Textures.tankOverlay, te.steam))
    widgets.add(new WidgetButtonIcon(Point(153, 18), openCfg, Textures.Button16.base, Textures.Button16.hover) {
      icon = Textures.Button16.wrench
      hover = Misc.toLocal("advgenerators.gui.output.title")
    })
    val steamTexture = new IconWrapper(Texture.BLOCKS, Blocks.steamFluid.getStillIcon)

    widgets.add(new WidgetLabel(Misc.toLocal("advgenerators.gui.turbine.title"), 8, 6, Color.darkGray))
    widgets.add(new WidgetLabel(Misc.toLocal("container.inventory"), 8, this.ySize - 96 + 3, Color.darkGray))
    widgets.add(new WidgetInfo(Rect(75, 21, 59, 10), Textures.Icons.turbine, te.numTurbines.value.toString, Misc.toLocal("advgenerators.label.turbine.turbines")))
    widgets.add(new WidgetInfo(Rect(75, 32, 59, 10), Textures.Icons.peak, DecFormat.short(te.numTurbines * te.cfg.mjPerTickPerTurbine * ratio) + " RF/t", Misc.toLocal("advgenerators.label.turbine.maxprod")))
    widgets.add(new WidgetInfo(Rect(75, 43, 59, 10), Textures.Icons.speed, DecFormat.short(te.speed) + " RPM", Misc.toLocal("advgenerators.label.turbine.rpm")))
    widgets.add(new WidgetInfo(Rect(75, 54, 59, 10), Textures.Icons.power, DecFormat.short(te.outputAverage.average * ratio) + " RF/t", Misc.toLocal("advgenerators.label.turbine.prod")))
    widgets.add(new WidgetInfo(Rect(75, 65, 59, 10), steamTexture, DecFormat.short(te.steamAverage.average) + " mB/t", Misc.toLocal("advgenerators.label.turbine.steamaverage")))
  }

  def openCfg(b: WidgetButtonIcon) {
    Client.minecraft.displayGuiScreen(new GuiOutputConfig(this))
  }
}
