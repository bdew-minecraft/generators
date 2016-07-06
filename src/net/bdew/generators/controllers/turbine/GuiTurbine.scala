/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.controllers.turbine

import net.bdew.generators.config.Config
import net.bdew.generators.gui.{GuiOutputConfig, GuiOutputFaces, WidgetPowerGaugeCustom, WidgetRateInfo}
import net.bdew.generators.modules.turbine.BlockTurbine
import net.bdew.generators.network.{NetworkHandler, PktDumpBuffers}
import net.bdew.generators.{Generators, Textures}
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.{WidgetButtonIcon, WidgetFluidGauge, WidgetLabel}
import net.bdew.lib.multiblock.gui.{WidgetInfo, WidgetInfoMulti}
import net.bdew.lib.{Client, DecFormat, Misc}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.text.TextFormatting

class GuiTurbine(val te: TileTurbineController, player: EntityPlayer) extends BaseScreen(new ContainerTurbine(te, player), 176, 175) with GuiOutputFaces {
  val background = Texture(Generators.modId, "textures/gui/turbine.png", rect)

  override def initGui() {
    super.initGui()
    widgets.add(new WidgetPowerGaugeCustom(new Rect(61, 19, 9, 58), Textures.powerFill, te.power))
    widgets.add(new WidgetFluidGauge(new Rect(9, 19, 9, 58), Textures.tankOverlay, te.fuel))

    widgets.add(new WidgetButtonIcon(Point(153, 19), openCfg, Textures.Button16.base, Textures.Button16.hover) {
      icon = Textures.Button16.wrench
      hover = Misc.toLocal("advgenerators.gui.output.title")
    })

    widgets.add(new WidgetButtonIcon(Point(153, 61), (b) => NetworkHandler.sendToServer(PktDumpBuffers()),
      Textures.Button16.base, Textures.Button16.red) {
      icon = Textures.Button16.disabled
      hover = Misc.toLocal("advgenerators.gui.dump")
    })

    widgets.add(new WidgetLabel(Misc.toLocal("advgenerators.gui.turbine.title"), 8, 6, Color.darkGray))
    widgets.add(new WidgetLabel(Misc.toLocal("container.inventory"), 8, this.ySize - 96 + 3, Color.darkGray))

    widgets.add(new WidgetInfoMulti(Rect(75, 21, 59, 10),
      Textures.Icons.turbine,
      te.numTurbines.value.toString,
      List(Misc.toLocal("advgenerators.label.turbine.turbines")) ++
        te.getModuleBlocks[BlockTurbine].values
          .groupBy(identity).mapValues(_.size).toList.sortBy(_._2)
          .map(x => "%d x %s".format(x._2, x._1.getLocalizedName))
    ))

    widgets.add(new WidgetInfo(Rect(75, 32, 59, 10),
      Textures.Icons.peak,
      "%s %s/t".format(DecFormat.short(te.maxMJPerTick * Config.powerShowMultiplier), Config.powerShowUnits),
      Misc.toLocal("advgenerators.label.turbine.maxprod"))
    )

    widgets.add(new WidgetInfoMulti(Rect(75, 43, 59, 10),
      Textures.Icons.fluid,
      DecFormat.short(te.fuelPerTick) + " mB/t",
      List(
        Misc.toLocal("advgenerators.label.turbine.fuel"),
        Misc.toLocalF("advgenerators.label.turbine.efficiency", "%s%.0f%%%s".format(TextFormatting.YELLOW, te.fuelEfficiency * 100, TextFormatting.RESET))
      )
    ))

    widgets.add(new WidgetInfo(Rect(75, 54, 59, 10),
      Textures.Icons.power,
      "%s %s/t".format(DecFormat.short(te.outputAverage.average * Config.powerShowMultiplier), Config.powerShowUnits),
      Misc.toLocal("advgenerators.label.turbine.prod"))
    )

    widgets.add(new WidgetRateInfo(Rect(75, 65, 59, 10),
      if (te.fuel.getFluid != null) Texture(te.fuel.getFluid.getFluid.getStill) else Texture(te.resources.disabled),
      if (te.fuel.getFluid != null) Color.fromInt(te.fuel.getFluid.getFluid.getColor) else Color.red,
      DecFormat.short(te.fuelPerTickAverage.average) + " mB/t",
      Misc.toLocal("advgenerators.label.turbine.fuelaverage")))
  }

  def openCfg(b: WidgetButtonIcon) {
    Client.minecraft.displayGuiScreen(new GuiOutputConfig(this))
  }
}
