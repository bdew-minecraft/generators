/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.nei

import java.util

import codechicken.nei.guihook.IContainerTooltipHandler
import net.bdew.generators.config.CarbonValueRegistry
import net.bdew.generators.controllers.syngas.GuiSyngas
import net.bdew.lib.{DecFormat, Misc}
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.item.ItemStack

object CarbonValueTooltipHandler extends IContainerTooltipHandler {
  override def handleTooltip(gui: GuiContainer, mouseX: Int, mouseY: Int, currentTip: util.List[String]) = currentTip
  override def handleItemDisplayName(gui: GuiContainer, itemStack: ItemStack, currentTip: util.List[String]) = currentTip
  override def handleItemTooltip(gui: GuiContainer, itemStack: ItemStack, mouseX: Int, mouseY: Int, currentTip: util.List[String]) = {
    if (gui.isInstanceOf[GuiSyngas] && itemStack != null && itemStack.getItem != null) {
      CarbonValueRegistry.getValueOpt(itemStack) map { cv =>
        currentTip.add(Misc.toLocalF("advgenerators.tooltip.carbon", DecFormat.short(cv)))
      }
    }
    currentTip
  }
}
