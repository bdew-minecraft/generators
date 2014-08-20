/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/generators/master/MMPL-1.0.txt
 */

package net.bdew.generators.nei

import java.text.DecimalFormat
import java.util

import codechicken.nei.guihook.IContainerTooltipHandler
import net.bdew.generators.blocks.turbineController.MachineTurbine
import net.bdew.lib.Misc
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.{FluidContainerRegistry, IFluidContainerItem}

object FuelTooltipHandler extends IContainerTooltipHandler {
  val DF = new DecimalFormat("#,###")
  override def handleTooltip(gui: GuiContainer, mousex: Int, mousey: Int, currenttip: util.List[String]) = currenttip
  override def handleItemDisplayName(gui: GuiContainer, itemstack: ItemStack, currenttip: util.List[String]) = currenttip
  override def handleItemTooltip(gui: GuiContainer, itemstack: ItemStack, mousex: Int, mousey: Int, currenttip: util.List[String]) = {
    if (itemstack != null && itemstack.getItem != null) {
      (if (FluidContainerRegistry.isContainer(itemstack))
        Option(FluidContainerRegistry.getFluidForFilledItem(itemstack)) flatMap (fs => Option(fs.getFluid))
      else {
        Misc.asInstanceOpt(itemstack.getItem, classOf[IFluidContainerItem]) flatMap { cont =>
          Option(cont.getFluid(itemstack)) flatMap (fs => Option(fs.getFluid))
        }
      }) map MachineTurbine.getFuelValue foreach (fv =>
        currenttip.add(Misc.toLocalF("advgenerators.tooltip.turbine.fuel", DF.format(fv / MachineTurbine.fuelConsumptionMultiplier ))))
    }
    currenttip
  }
}
