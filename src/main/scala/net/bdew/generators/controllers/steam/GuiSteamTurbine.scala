package net.bdew.generators.controllers.steam

import net.bdew.generators.gui.GuiOutputConfig
import net.bdew.generators.modules.turbine.BlockTurbine
import net.bdew.generators.network.{NetworkHandler, PktDumpBuffers}
import net.bdew.generators.registries.Fluids
import net.bdew.generators.{Generators, Textures}
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.{WidgetButtonIcon, WidgetFluidGauge, WidgetLabel}
import net.bdew.lib.multiblock.gui.{WidgetInfo, WidgetInfoMulti}
import net.bdew.lib.power.WidgetPowerGauge
import net.bdew.lib.{Client, Text}
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class GuiSteamTurbine(container: ContainerSteamTurbine, playerInv: Inventory) extends BaseScreen(container, playerInv, container.te.getDisplayName) {
  val te: TileSteamTurbineController = container.te
  val background: Sprite = Texture(Generators.ModId, "textures/gui/turbine.png", Rect(0, 0, 176, 175))

  val steamTexture: Sprite = Texture(Fluids.steam.source.get().getAttributes.getStillTexture)

  override def init(): Unit = {
    initGui(176, 175)

    widgets.add(new WidgetPowerGauge(BaseRect(61, 19, 9, 58), Textures.powerFill, te.power))
    widgets.add(new WidgetFluidGauge(new Rect(9, 19, 9, 58), Textures.tankOverlay, te.steam))

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

    widgets.add(new WidgetInfoMulti(Rect(75, 21, 59, 10),
      Textures.Icons.turbine,
      Text.string(te.numTurbines.value.toString),
      List(Text.translate("advgenerators.label.turbine.turbines")) ++
        te.getModuleBlocks[BlockTurbine].values
          .groupBy(identity).map(x => x._1 -> x._2.size).toList.sortBy(_._2)
          .map(x => Text.translate("advgenerators.gui.module.count", x._2.toString, x._1.getName))
    ))

    widgets.add(new WidgetInfo(Rect(75, 32, 59, 10),
      Textures.Icons.peak,
      Text.energyPerTick(te.maxFEPerTick),
      Text.translate("advgenerators.label.turbine.maxprod")
    ))

    widgets.add(new WidgetInfo(Rect(75, 43, 59, 10),
      Textures.Icons.speed,
      Text.amount(te.speed, "rpm"),
      Text.translate("advgenerators.label.turbine.rpm")
    ))

    widgets.add(new WidgetInfo(Rect(75, 54, 59, 10),
      Textures.Icons.power,
      Text.energyPerTick(te.outputAverage.average),
      Text.translate("advgenerators.label.turbine.prod"))
    )

    widgets.add(new WidgetInfo(Rect(75, 65, 59, 10),
      steamTexture,
      Text.fluidPerTick(te.steamAverage.average),
      Text.translate("advgenerators.label.turbine.steamaverage"))
    )
  }

  def openCfg(b: WidgetButtonIcon): Unit = {
    Client.minecraft.setScreen(new GuiOutputConfig(this, te, playerInv))
  }
}
