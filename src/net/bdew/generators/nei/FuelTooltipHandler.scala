/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.nei

import java.util

import codechicken.nei.guihook.IContainerTooltipHandler
import net.bdew.generators.config.{Config, TurbineFuel}
import net.bdew.generators.controllers.turbine.MachineTurbine
import net.bdew.lib.{DecFormat, Misc}
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.{FluidContainerRegistry, IFluidContainerItem}

object FuelTooltipHandler extends IContainerTooltipHandler {
  override def handleTooltip(gui: GuiContainer, mouseX: Int, mouseY: Int, currentTip: util.List[String]) = currentTip
  override def handleItemDisplayName(gui: GuiContainer, itemStack: ItemStack, currentTip: util.List[String]) = currentTip
  override def handleItemTooltip(gui: GuiContainer, itemStack: ItemStack, mouseX: Int, mouseY: Int, currentTip: util.List[String]) = {
    if (itemStack != null && itemStack.getItem != null) {
      (if (FluidContainerRegistry.isContainer(itemStack))
        Option(FluidContainerRegistry.getFluidForFilledItem(itemStack)) flatMap (fs => Option(fs.getFluid))
      else {
        Misc.asInstanceOpt(itemStack.getItem, classOf[IFluidContainerItem]) flatMap { cont =>
          Option(cont.getFluid(itemStack)) flatMap (fs => Option(fs.getFluid))
        }
      }) map TurbineFuel.getFuelValue filter (_ > 0) foreach (fv =>
        currentTip.add(Misc.toLocalF("advgenerators.tooltip.turbine.fuel", DecFormat.round(fv / MachineTurbine.fuelConsumptionMultiplier * Config.powerShowMultiplier), Config.powerShowUnits)))
    }
    currentTip
  }
}
