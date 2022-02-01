package net.bdew.generators.controllers.exchanger

import net.bdew.generators.gui.{GuiOutputConfig, WidgetRateInfo, WidgetResourceGauge}
import net.bdew.generators.network.{NetworkHandler, PktDumpBuffers}
import net.bdew.generators.{Generators, Textures}
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.{WidgetButtonIcon, WidgetLabel}
import net.bdew.lib.multiblock.gui.WidgetInfo
import net.bdew.lib.resource.{FluidResource, ResourceKind}
import net.bdew.lib.{Client, Text}
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class GuiExchanger(container: ContainerExchanger, playerInv: Inventory) extends BaseScreen(container, playerInv, container.te.getDisplayName) {
  val te: TileExchangerController = container.te
  val background: Texture = Texture(Generators.ModId, "textures/gui/exchanger.png", Rect(0, 0, 176, 175))

  override def init(): Unit = {
    initGui(176, 175)
    widgets.add(new WidgetResourceGauge(new Rect(9, 19, 9, 58), Textures.tankOverlay, te.coolerIn))
    widgets.add(new WidgetResourceGauge(new Rect(21, 19, 37, 16), null, te.heaterIn))
    widgets.add(new WidgetResourceGauge(new Rect(61, 19, 9, 58), Textures.tankOverlay, te.coolerOut))
    widgets.add(new WidgetResourceGauge(new Rect(21, 61, 37, 16), null, te.heaterOut))

    widgets.add(new WidgetButtonIcon(Point(153, 19), openCfg, Textures.Button16.base, Textures.Button16.hover) {
      override val icon: Texture = Textures.Button16.wrench
      override val hover: Component = Text.translate("advgenerators.gui.output.title")
    })

    widgets.add(new WidgetButtonIcon(Point(153, 61), _ => NetworkHandler.sendToServer(PktDumpBuffers()), Textures.Button16.base, Textures.Button16.red) {
      override val icon: Texture = Textures.Button16.disabled
      override val hover: Component = Text.translate("advgenerators.gui.dump")
    })

    widgets.add(new WidgetLabel(title, 8, 6, Color.darkGray))
    widgets.add(new WidgetLabel(Text.translate("container.inventory"), 8, rect.h - 96 + 3, Color.darkGray))

    widgets.add(new WidgetInfo(Rect(75, 21, 59, 10), Textures.Icons.temperature,
      Text.amount(te.heat.value, "hu"),
      Text.translate("advgenerators.label.exchanger.heat")))

    widgets.add(new WidgetInfo(Rect(75, 32, 59, 10), Textures.Icons.heatExchange,
      Text.perTick(te.maxHeatTransfer.value, "hu"),
      Text.translate("advgenerators.label.exchanger.maxtransfer")))

    widgets.add(new WidgetInfo(Rect(75, 43, 59, 10), Textures.Icons.heatLoss,
      Text.perTick(te.heatLoss.average, "hu"),
      Text.translate("advgenerators.label.exchanger.heatloss")))

    widgets.add(new WidgetRateInfo(Rect(75, 54, 59, 10),
      te.lastInput map (_.getTexture) getOrElse Texture(te.resources.disabled),
      te.lastInput map (_.getColor) getOrElse Color.red,
      formatFlowRate(te.lastInput, te.inputRate.average),
      te.lastInput map (x => Text.translate("advgenerators.label.exchanger.inrate", x.getName))
        getOrElse Text.translate("advgenerators.label.exchanger.noinput")
    ))

    widgets.add(new WidgetRateInfo(Rect(75, 65, 59, 10),
      te.lastOutput map (_.getTexture) getOrElse Texture(te.resources.disabled),
      te.lastOutput map (_.getColor) getOrElse Color.red,
      formatFlowRate(te.lastOutput, te.outputRate.average),
      te.lastOutput map (x => Text.translate("advgenerators.label.exchanger.outrate", x.getName))
        getOrElse Text.translate("advgenerators.label.exchanger.nooutput")
    ))
  }

  def formatFlowRate(r: Option[ResourceKind], v: Double): Component = r match {
    case Some(FluidResource(_)) =>
      Text.fluidPerTick(v)
    case Some(_) =>
      Text.perTick(v)
    case _ => Text.empty
  }

  def openCfg(b: WidgetButtonIcon): Unit = {
    Client.minecraft.setScreen(new GuiOutputConfig(this, te, playerInv))
  }
}
