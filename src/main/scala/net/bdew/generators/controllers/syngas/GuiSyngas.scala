package net.bdew.generators.controllers.syngas

import com.mojang.blaze3d.vertex.PoseStack
import net.bdew.generators.gui.{GuiOutputConfig, WidgetFillDataSlotTooltip}
import net.bdew.generators.network.{NetworkHandler, PktDumpBuffers}
import net.bdew.generators.registries.Fluids
import net.bdew.generators.{Generators, Textures}
import net.bdew.lib.Text.pimpTextComponent
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.{WidgetButtonIcon, WidgetFluidGauge, WidgetLabel}
import net.bdew.lib.multiblock.gui.WidgetInfo
import net.bdew.lib.{Client, DecFormat, Text}
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraftforge.client.RenderProperties

import scala.collection.mutable.ArrayBuffer

class GuiSyngas(container: ContainerSyngas, playerInv: Inventory) extends BaseScreen(container, playerInv, container.te.getDisplayName) {
  val te: TileSyngasController = container.te

  val background: Texture = Texture(Generators.ModId, "textures/gui/syngas.png", Rect(0, 0, 176, 175))

  val coalTexture: IconWrapper = Texture.block("minecraft:block/coal_block")
  val steamTexture: Sprite = Texture(RenderProperties.get(Fluids.steam.source.get()).getStillTexture)
  val syngasTexture: Sprite = Texture(RenderProperties.get(Fluids.syngas.source.get()).getStillTexture)

  def bufferTooltip =
    List(
      Text.translate("advgenerators.label.syngas.steam", DecFormat.short(te.steamBuffer / te.cfg.internalTankCapacity() * 100) + "%"),
      Text.translate("advgenerators.label.syngas.carbon", DecFormat.short(te.carbonBuffer / te.cfg.internalTankCapacity() * 100) + "%")
    )

  override def init(): Unit = {
    initGui(176, 175)

    widgets.add(new WidgetFluidGauge(new Rect(11, 19, 10, 35), Textures.tankOverlaySmaller, te.waterTank))
    widgets.add(new WidgetFluidGauge(new Rect(57, 19, 10, 35), Textures.tankOverlaySmaller, te.syngasTank))

    widgets.add(new WidgetFillDataSlotTooltip(
      Rect(30, 61, 37, 8), steamTexture, Direction.DOWN, te.steamBuffer, te.cfg.internalTankCapacity(), bufferTooltip))

    widgets.add(new WidgetFillDataSlotTooltip(
      Rect(30, 69, 37, 8), coalTexture, Direction.UP, te.carbonBuffer, te.cfg.internalTankCapacity(), bufferTooltip))

    widgets.add(new WidgetFillDataSlotTooltip(
      Rect(9, 62, 14, 14), Textures.Icons.fire, Direction.UP, te.heat, te.cfg.maxHeat(),
      List(Text.translate("advgenerators.label.syngas.heat", DecFormat.round(te.heat), DecFormat.round(te.cfg.maxHeat())))
    ) {
      override def handleTooltip(p: Point, tip: ArrayBuffer[Component]): Unit = {
        if (te.heatingChambers > 0)
          super.handleTooltip(p, tip)
        else
          tip += Text.translate("advgenerators.label.syngas.heat.disabled").setColor(Text.Color.RED)
      }

      override def draw(m: PoseStack, mouse: Point, partial: Float): Unit = {
        if (te.heatingChambers > 0)
          super.draw(m, mouse, partial)
        else
          parent.drawTexture(m, rect, Textures.Button16.disabled)
      }
    })

    widgets.add(new WidgetButtonIcon(Point(153, 19), openCfg, Textures.Button16.base, Textures.Button16.hover) {
      override val icon: Texture = Textures.Button16.wrench
      override val hover: Component = Text.translate("advgenerators.gui.output.title")
    })

    widgets.add(new WidgetButtonIcon(Point(153, 61), _ => NetworkHandler.sendToServer(PktDumpBuffers()),
      Textures.Button16.base, Textures.Button16.red) {
      override val icon: Texture = Textures.Button16.disabled
      override val hover: Component = Text.translate("advgenerators.gui.dump")
    })

    widgets.add(new WidgetLabel(title, 8, 6, Color.darkGray))
    widgets.add(new WidgetLabel(Text.translate("container.inventory"), 8, rect.h - 96 + 3, Color.darkGray))

    widgets.add(new WidgetInfo(Rect(75, 21, 59, 10),
      Textures.Icons.heatExchange,
      if (te.heatingChambers > 0)
        Text.delta(te.avgHeatDelta.average, "hu")
      else
        Text.string("-----"),
      Text.translate("advgenerators.label.syngas.heat.delta"))
    )

    widgets.add(new WidgetInfo(Rect(75, 32, 59, 10),
      coalTexture,
      Text.perTick(te.avgCarbonUsed.average, "carbon_short"),
      Text.translate("advgenerators.label.syngas.consumed"))
    )

    widgets.add(new WidgetInfo(Rect(75, 43, 59, 10),
      syngasTexture,
      Text.fluidPerTick(te.avgSyngasProduced.average),
      Text.translate("advgenerators.label.syngas.produced"))
    )
  }

  def openCfg(b: WidgetButtonIcon): Unit = {
    Client.minecraft.setScreen(new GuiOutputConfig(this, te, playerInv))
  }
}
