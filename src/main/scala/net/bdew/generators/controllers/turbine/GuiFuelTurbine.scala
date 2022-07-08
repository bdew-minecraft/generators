package net.bdew.generators.controllers.turbine

import net.bdew.generators.gui.{GuiOutputConfig, WidgetRateInfo}
import net.bdew.generators.modules.turbine.BlockTurbine
import net.bdew.generators.network.{NetworkHandler, PktDumpBuffers}
import net.bdew.generators.{Generators, Textures}
import net.bdew.lib.Text.pimpTextComponent
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.{WidgetButtonIcon, WidgetFluidGauge, WidgetLabel}
import net.bdew.lib.multiblock.gui.{WidgetInfo, WidgetInfoMulti}
import net.bdew.lib.power.WidgetPowerGauge
import net.bdew.lib.{Client, Text}
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions

class GuiFuelTurbine(container: ContainerFuelTurbine, playerInv: Inventory) extends BaseScreen(container, playerInv, container.te.getDisplayName) {
  val te: TileFuelTurbineController = container.te
  val background: Texture = Texture(Generators.ModId, "textures/gui/turbine.png", Rect(0, 0, 176, 175))

  override def init(): Unit = {
    initGui(176, 175)
    widgets.add(new WidgetPowerGauge(new Rect(61, 19, 9, 58), Textures.powerFill, te.power))
    widgets.add(new WidgetFluidGauge(new Rect(9, 19, 9, 58), Textures.tankOverlay, te.fuel))

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
      Text.translate("advgenerators.label.turbine.maxprod"))
    )

    widgets.add(new WidgetInfoMulti(Rect(75, 43, 59, 10),
      Textures.Icons.fluid,
      Text.fluidPerTick(te.fuelPerTick),
      List(
        Text.translate("advgenerators.label.turbine.fuel"),
        Text.translate("advgenerators.label.turbine.efficiency",
          Text.string("%.0f%%".format(te.fuelEfficiency * 100)).setColor(Text.Color.YELLOW)
        )
      )
    ))

    widgets.add(new WidgetInfo(Rect(75, 54, 59, 10),
      Textures.Icons.power,
      Text.energyPerTick(te.outputAverage.average),
      Text.translate("advgenerators.label.turbine.prod"))
    )

    widgets.add(new WidgetRateInfo(Rect(75, 65, 59, 10),
      if (!te.fuel.getFluid.isEmpty) Texture(IClientFluidTypeExtensions.of(te.fuel.getFluid.getFluid).getStillTexture(te.fuel.getFluid)) else Texture(te.resources.disabled),
      if (!te.fuel.getFluid.isEmpty) Color.fromInt(IClientFluidTypeExtensions.of(te.fuel.getFluid.getFluid).getTintColor(te.fuel.getFluid)) else Color.red,
      Text.fluidPerTick(te.fuelPerTickAverage.average),
      Text.translate("advgenerators.label.turbine.fuelaverage")))
  }

  def openCfg(b: WidgetButtonIcon): Unit = {
    Client.minecraft.setScreen(new GuiOutputConfig(this, te, playerInv))
  }
}
