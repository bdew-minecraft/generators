/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.controllers.exchanger

import net.bdew.generators.gui.{GuiOutputConfig, GuiOutputFaces, WidgetRateInfo, WidgetResourceGauge}
import net.bdew.generators.{Generators, IconCache, Textures}
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.{WidgetButtonIcon, WidgetLabel}
import net.bdew.lib.multiblock.gui.WidgetInfo
import net.bdew.lib.resource.{FluidResource, Resource}
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
    widgets.add(new WidgetLabel(Misc.toLocal("advgenerators.gui.exchanger.title"), 8, 6, Color.darkGray))
    widgets.add(new WidgetLabel(Misc.toLocal("container.inventory"), 8, this.ySize - 96 + 3, Color.darkGray))

    widgets.add(new WidgetInfo(Rect(75, 21, 59, 10), Textures.Icons.turbine,
      DecFormat.short(te.heat.value) + " HU",
      Misc.toLocal("advgenerators.label.exchanger.heat")))

    widgets.add(new WidgetInfo(Rect(75, 32, 59, 10), Textures.Icons.peak,
      DecFormat.short(te.maxHeatTransfer.value) + " HU/t",
      Misc.toLocal("advgenerators.label.exchanger.maxtransfer")))

    widgets.add(new WidgetInfo(Rect(75, 43, 59, 10), Textures.Icons.peak,
      DecFormat.short(te.heatLoss.average) + " HU/t",
      Misc.toLocal("advgenerators.label.exchanger.heatloss")))

    widgets.add(new WidgetRateInfo(Rect(75, 54, 59, 10),
      te.heaterIn.resource map (_.kind.getTexture) getOrElse IconCache.disabled,
      te.heaterIn.resource map (_.kind.getColor) getOrElse Color.red,
      formatFlowRate(te.heaterIn.resource, te.inputRate.average),
      te.heaterIn.resource map (x => Misc.toLocalF("advgenerators.label.exchanger.inrate", x.kind.getLocalizedName))
        getOrElse Misc.toLocal("advgenerators.label.exchanger.noinput")
    ))

    widgets.add(new WidgetRateInfo(Rect(75, 65, 59, 10),
      te.coolerOut.resource map (_.kind.getTexture) getOrElse IconCache.disabled,
      te.coolerOut.resource map (_.kind.getColor) getOrElse Color.red,
      formatFlowRate(te.coolerOut.resource, te.outputRate.average),
      te.coolerOut.resource map (x => Misc.toLocalF("advgenerators.label.exchanger.outrate", x.kind.getLocalizedName))
        getOrElse Misc.toLocal("advgenerators.label.exchanger.nooutput")
    ))
  }

  def formatFlowRate(r: Option[Resource], v: Double) = r match {
    case Some(Resource(FluidResource(_), _)) =>
      Misc.toLocalF("advgenerators.flow.fluid", DecFormat.short(v))
    case Some(Resource(_, _)) =>
      Misc.toLocalF("advgenerators.flow.other", DecFormat.short(v))
    case _ => ""
  }

  def openCfg(b: WidgetButtonIcon) {
    Client.minecraft.displayGuiScreen(new GuiOutputConfig(this))
  }
}
