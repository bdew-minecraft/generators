/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.controllers.exchanger

import net.bdew.generators.gui.{GuiOutputConfig, GuiOutputFaces, WidgetResourceGauge}
import net.bdew.generators.{Generators, Textures}
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.{WidgetButtonIcon, WidgetLabel}
import net.bdew.lib.multiblock.gui.WidgetInfo
import net.bdew.lib.{Client, DecFormat, Misc}
import net.minecraft.entity.player.EntityPlayer

class GuiExchanger(val te: TileExchangerController, player: EntityPlayer) extends BaseScreen(new ContainerExchanger(te, player), 176, 175) with GuiOutputFaces {
  val background = Texture(Generators.modId, "textures/gui/exchanger.png", rect)
  override def initGui() {
    super.initGui()
    widgets.add(new WidgetResourceGauge(new Rect(9, 19, 9, 58), Textures.tankOverlay, te.coolerIn))
    widgets.add(new WidgetResourceGauge(new Rect(21, 19, 37, 16), null, te.heaterIn))
    widgets.add(new WidgetResourceGauge(new Rect(61, 19, 9, 58), Textures.tankOverlay, te.coolerOut))
    widgets.add(new WidgetResourceGauge(new Rect(21, 61, 37, 16), null, te.heaterOut))
    widgets.add(new WidgetButtonIcon(Point(153, 18), openCfg, Textures.Button16.base, Textures.Button16.hover) {
      icon = Textures.Button16.wrench
      hover = Misc.toLocal("advgenerators.gui.output.title")
    })
    widgets.add(new WidgetLabel(Misc.toLocal("advgenerators.gui.turbine.title"), 8, 6, Color.darkgray))
    widgets.add(new WidgetLabel(Misc.toLocal("container.inventory"), 8, this.ySize - 96 + 3, Color.darkgray))

    widgets.add(new WidgetInfo(Rect(75, 21, 59, 10), Textures.Icons.turbine,
      DecFormat.round(te.heat.cval) + " HU",
      Misc.toLocal("advgenerators.label.exchanger.heat")))

    widgets.add(new WidgetInfo(Rect(75, 32, 59, 10), Textures.Icons.peak,
      DecFormat.dec2(te.maxHeatTransfer.cval) + " HU/t",
      Misc.toLocal("advgenerators.label.exchanger.maxtransfer")))
  }

  def openCfg(b: WidgetButtonIcon) {
    Client.minecraft.displayGuiScreen(new GuiOutputConfig(this))
  }
}
