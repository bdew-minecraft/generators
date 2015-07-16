/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.gui

import net.bdew.generators.config.{Config, TurbineFuel}
import net.bdew.generators.controllers.turbine.{GuiTurbine, MachineTurbine}
import net.bdew.lib.tooltip.FluidTooltipProvider
import net.bdew.lib.{Client, DecFormat, Misc}
import net.minecraftforge.fluids.FluidStack

object FuelTooltipProvider extends FluidTooltipProvider {
  override def shouldHandleTooltip(fluid: FluidStack): Boolean =
    (Config.alwaysShowFuelTooltip || Client.minecraft.currentScreen.isInstanceOf[GuiTurbine]) && TurbineFuel.getFuelValue(fluid.getFluid) > 0

  override def handleTooltip(fluid: FluidStack, advanced: Boolean, shift: Boolean): Iterable[String] = {
    List(
      Misc.toLocalF("advgenerators.tooltip.turbine.fuel",
        DecFormat.round(TurbineFuel.getFuelValue(fluid.getFluid) / MachineTurbine.fuelConsumptionMultiplier * Config.powerShowMultiplier), Config.powerShowUnits)
    )
  }
}
