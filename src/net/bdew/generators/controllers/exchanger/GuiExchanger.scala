/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.controllers.exchanger

import net.bdew.generators.gui.{GuiOutputConfig, GuiOutputFaces, WidgetRateInfo, WidgetResourceGauge}
import net.bdew.generators.network.{NetworkHandler, PktDumpBuffers}
import net.bdew.generators.{Generators, Textures}
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.{WidgetButtonIcon, WidgetLabel}
import net.bdew.lib.multiblock.gui.WidgetInfo
import net.bdew.lib.resource.{FluidResource, ResourceKind}
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

    widgets.add(new WidgetButtonIcon(Point(153, 19), openCfg, Textures.Button16.base, Textures.Button16.hover) {
      icon = Textures.Button16.wrench
      hover = Misc.toLocal("advgenerators.gui.output.title")
    })

    widgets.add(new WidgetButtonIcon(Point(153, 61), (b) => NetworkHandler.sendToServer(PktDumpBuffers()),
      Textures.Button16.base, Textures.Button16.red) {
      icon = Textures.Button16.disabled
      hover = Misc.toLocal("advgenerators.gui.dump")
    })

    widgets.add(new WidgetLabel(Misc.toLocal("advgenerators.gui.exchanger.title"), 8, 6, Color.darkGray))
    widgets.add(new WidgetLabel(Misc.toLocal("container.inventory"), 8, this.ySize - 96 + 3, Color.darkGray))

    widgets.add(new WidgetInfo(Rect(75, 21, 59, 10), Textures.Icons.temperature,
      DecFormat.short(te.heat.value) + " HU",
      Misc.toLocal("advgenerators.label.exchanger.heat")))

    widgets.add(new WidgetInfo(Rect(75, 32, 59, 10), Textures.Icons.heatExchange,
      DecFormat.short(te.maxHeatTransfer.value) + " HU/t",
      Misc.toLocal("advgenerators.label.exchanger.maxtransfer")))

    widgets.add(new WidgetInfo(Rect(75, 43, 59, 10), Textures.Icons.heatLoss,
      DecFormat.short(te.heatLoss.average) + " HU/t",
      Misc.toLocal("advgenerators.label.exchanger.heatloss")))

    widgets.add(new WidgetRateInfo(Rect(75, 54, 59, 10),
      te.lastInput map (_.getTexture) getOrElse Texture(te.resources.disabled),
      te.lastInput map (_.getColor) getOrElse Color.red,
      formatFlowRate(te.lastInput, te.inputRate.average),
      te.lastInput map (x => Misc.toLocalF("advgenerators.label.exchanger.inrate", x.getLocalizedName))
        getOrElse Misc.toLocal("advgenerators.label.exchanger.noinput")
    ))

    widgets.add(new WidgetRateInfo(Rect(75, 65, 59, 10),
      te.lastOutput map (_.getTexture) getOrElse Texture(te.resources.disabled),
      te.lastOutput map (_.getColor) getOrElse Color.red,
      formatFlowRate(te.lastOutput, te.outputRate.average),
      te.lastOutput map (x => Misc.toLocalF("advgenerators.label.exchanger.outrate", x.getLocalizedName))
        getOrElse Misc.toLocal("advgenerators.label.exchanger.nooutput")
    ))
  }

  def formatFlowRate(r: Option[ResourceKind], v: Double) = r match {
    case Some(FluidResource(_)) =>
      Misc.toLocalF("advgenerators.flow.fluid", DecFormat.short(v))
    case Some(x: ResourceKind) =>
      Misc.toLocalF("advgenerators.flow.other", DecFormat.short(v))
    case _ => ""
  }

  def openCfg(b: WidgetButtonIcon) {
    Client.minecraft.displayGuiScreen(new GuiOutputConfig(this))
  }
}
