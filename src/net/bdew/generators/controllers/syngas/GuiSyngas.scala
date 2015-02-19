/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.controllers.syngas

import net.bdew.generators.gui.{GuiOutputConfig, GuiOutputFaces, WidgetFillDataSlotTooltip}
import net.bdew.generators.network.{NetworkHandler, PktDumpBuffers}
import net.bdew.generators.{Generators, Textures, config}
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.{WidgetButtonIcon, WidgetFluidGauge, WidgetLabel}
import net.bdew.lib.multiblock.gui.WidgetInfo
import net.bdew.lib.{Client, DecFormat, Misc}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks

class GuiSyngas(val te: TileSyngasController, player: EntityPlayer) extends BaseScreen(new ContainerSyngas(te, player), 176, 175) with GuiOutputFaces {
  val background = Texture(Generators.modId, "textures/gui/syngas.png", rect)

  val coalTexture = new IconWrapper(Texture.BLOCKS, Blocks.coal_block.getIcon(0, 0))
  val steamTexture = new IconWrapper(Texture.BLOCKS, config.Blocks.steamFluid.getStillIcon)
  val syngasTexture = new IconWrapper(Texture.BLOCKS, config.Blocks.syngasFluid.getStillIcon)

  def bufferTooltip =
    List(
      Misc.toLocalF("advgenerators.label.syngas.steam", DecFormat.round(te.steamBuffer), DecFormat.round(te.cfg.internalTankCapacity)),
      Misc.toLocalF("advgenerators.label.syngas.carbon", DecFormat.round(te.carbonBuffer), DecFormat.round(te.cfg.internalTankCapacity))
    )

  override def initGui() {
    super.initGui()

    widgets.add(new WidgetFluidGauge(new Rect(11, 19, 10, 35), Textures.tankOverlaySmaller, te.waterTank))
    widgets.add(new WidgetFluidGauge(new Rect(57, 19, 10, 35), Textures.tankOverlaySmaller, te.syngasTank))

    widgets.add(new WidgetFillDataSlotTooltip(
      Rect(30, 61, 37, 8), steamTexture, Direction.DOWN, te.steamBuffer, te.cfg.internalTankCapacity, bufferTooltip))

    widgets.add(new WidgetFillDataSlotTooltip(
      Rect(30, 69, 37, 8), coalTexture, Direction.UP, te.carbonBuffer, te.cfg.internalTankCapacity, bufferTooltip))

    widgets.add(new WidgetFillDataSlotTooltip(
      Rect(9, 62, 14, 14), Textures.Icons.fire, Direction.UP, te.heat, te.cfg.maxHeat,
      List(Misc.toLocalF("advgenerators.label.syngas.heat", DecFormat.round(te.heat), DecFormat.round(te.cfg.maxHeat)))
    ))

    widgets.add(new WidgetButtonIcon(Point(153, 19), openCfg, Textures.Button16.base, Textures.Button16.hover) {
      icon = Textures.Button16.wrench
      hover = Misc.toLocal("advgenerators.gui.output.title")
    })

    widgets.add(new WidgetButtonIcon(Point(153, 61), (b) => NetworkHandler.sendToServer(PktDumpBuffers()),
      Textures.Button16.base, Textures.Button16.red) {
      icon = Textures.Button16.disabled
      hover = Misc.toLocal("advgenerators.gui.dump")
    })

    widgets.add(new WidgetLabel(Misc.toLocal("advgenerators.gui.syngas.title"), 8, 6, Color.darkGray))
    widgets.add(new WidgetLabel(Misc.toLocal("container.inventory"), 8, this.ySize - 96 + 3, Color.darkGray))

    //    widgets.add(new WidgetInfo(Rect(75, 21, 59, 10),
    //      Textures.Icons.turbine,
    //      te.numTurbines.value.toString,
    //      Misc.toLocal("advgenerators.label.turbine.turbines"))
    //    )
    //
    widgets.add(new WidgetInfo(Rect(75, 32, 59, 10),
      coalTexture,
      DecFormat.short(te.avgCarbonUsed.average) + " C/t",
      Misc.toLocal("advgenerators.label.syngas.consumed"))
    )

    widgets.add(new WidgetInfo(Rect(75, 43, 59, 10),
      syngasTexture,
      DecFormat.short(te.avgSyngasProduced.average) + " mB/t",
      Misc.toLocal("advgenerators.label.syngas.produced"))
    )
    //
    //    widgets.add(new WidgetInfo(Rect(75, 54, 59, 10),
    //      Textures.Icons.power,
    //      "%s %s/t".format(DecFormat.short(te.outputAverage.average * Config.powerShowMultiplier), Config.powerShowUnits),
    //      Misc.toLocal("advgenerators.label.turbine.prod"))
    //    )
    //
    //    widgets.add(new WidgetRateInfo(Rect(75, 65, 59, 10),
    //      if (te.fuel.getFluid != null) new IconWrapper(Texture.BLOCKS, te.fuel.getFluid.getFluid.getStillIcon) else IconCache.disabled,
    //      if (te.fuel.getFluid != null) Color.fromInt(te.fuel.getFluid.getFluid.getColor) else Color.red,
    //      DecFormat.short(te.fuelPerTickAverage.average) + " mB/t",
    //      Misc.toLocal("advgenerators.label.turbine.fuelaverage")))
  }

  def openCfg(b: WidgetButtonIcon) {
    Client.minecraft.displayGuiScreen(new GuiOutputConfig(this))
  }
}
