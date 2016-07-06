/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.gui

import net.bdew.generators.config.{CarbonValueRegistry, Config}
import net.bdew.generators.controllers.syngas.GuiSyngas
import net.bdew.lib.tooltip.TooltipProvider
import net.bdew.lib.{Client, DecFormat, Misc}
import net.minecraft.item.ItemStack

object CarbonValueTooltipProvider extends TooltipProvider {
  override def shouldHandleTooltip(stack: ItemStack): Boolean =
    (Config.alwaysShowFuelTooltip || Client.minecraft.currentScreen.isInstanceOf[GuiSyngas]) && CarbonValueRegistry.getValueOpt(stack).isDefined

  override def handleTooltip(stack: ItemStack, advanced: Boolean, shift: Boolean): Iterable[String] =
    CarbonValueRegistry.getValueOpt(stack) map (cv => Misc.toLocalF("advgenerators.tooltip.carbon", DecFormat.short(cv)))
}
