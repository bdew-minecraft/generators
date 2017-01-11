/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.gui

import net.bdew.generators.Generators
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.WidgetLabel
import net.bdew.lib.multiblock.interact.CIOutputFaces
import net.bdew.lib.{Client, Misc}
import net.minecraft.entity.player.EntityPlayer

class ContainerOutputConfig extends NoInvContainer {
  override def canInteractWith(player: EntityPlayer) = true
}

trait GuiOutputFaces extends BaseScreen {
  val te: CIOutputFaces
}

class GuiOutputConfig(parent: GuiOutputFaces) extends BaseScreen(new ContainerOutputConfig, 124, 138) {
  val background = Texture(Generators.modId, "textures/gui/outputs.png", rect)
  override def initGui() {
    super.initGui()
    widgets.add(new WidgetOutputs(Point(7, 18), parent.te, 6))
    widgets.add(new WidgetLabel(Misc.toLocal("advgenerators.gui.output.title"), 8, 6, Color.darkGray))
  }

  override def keyTyped(char: Char, keyCode: Int) {
    if (keyCode == 1 || keyCode == Client.minecraft.gameSettings.keyBindInventory.getKeyCode) {
      Client.minecraft.displayGuiScreen(parent)
    } else super.keyTyped(char, keyCode)
  }
}
