/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.sensor

import net.bdew.generators.Generators
import net.bdew.lib.Misc
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.WidgetLabel
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks

class GuiSensor(val te: TileSensor, player: EntityPlayer) extends BaseScreen(new ContainerSensor(te, player), 176, 175) {
  val background = Texture(Generators.modId, "textures/gui/sensor.png", rect)

  override def initGui() {
    super.initGui()

    widgets.add(new WidgetSensorIcon(Point(53, 38),
      te.config.sensor.texture, Color.white,
      te.config.sensor.localizedName))

    widgets.add(new WidgetSensorIcon(Point(71, 38),
      te.config.param.texture, Color.white,
      te.config.param.localizedName))

    val rsOn = new IconWrapper(Texture.BLOCKS, Blocks.redstone_torch.getIcon(0, 0))
    val rsOff = new IconWrapper(Texture.BLOCKS, Blocks.unlit_redstone_torch.getIcon(0, 0))

    widgets.add(new WidgetSensorIcon(Point(107, 38),
      if (te.isSignalOn) rsOn else rsOff,
      Color.white,
      if (te.isSignalOn) Misc.toLocal("advgenerators.label.sensor.on") else Misc.toLocal("advgenerators.label.sensor.off")))

    widgets.add(new WidgetLabel(Misc.toLocal("advgenerators.gui.sensor.title"), 8, 6, Color.darkGray))
    widgets.add(new WidgetLabel(Misc.toLocal("container.inventory"), 8, this.ySize - 96 + 3, Color.darkGray))
  }
}

