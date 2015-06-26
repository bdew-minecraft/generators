/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.control

import net.bdew.generators.Generators
import net.bdew.generators.control.{WidgetControlAction, WidgetControlMode}
import net.bdew.lib.Misc
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.WidgetLabel
import net.minecraft.entity.player.EntityPlayer

class GuiControl(val te: TileControl, player: EntityPlayer) extends BaseScreen(new ContainerControl(te, player), 176, 175) {
  val background = Texture(Generators.modId, "textures/gui/control.png", rect)

  override def initGui() {
    super.initGui()

    widgets.add(new WidgetControlAction(Point(98, 38), te.action))
    widgets.add(new WidgetControlMode(Point(62, 38), te.mode))

    widgets.add(new WidgetLabel(Misc.toLocal("advgenerators.gui.control.title"), 8, 6, Color.darkGray))
    widgets.add(new WidgetLabel(Misc.toLocal("container.inventory"), 8, this.ySize - 96 + 3, Color.darkGray))
  }
}

