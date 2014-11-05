/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.config.loader

import net.bdew.generators.Generators
import net.bdew.generators.config.{Tuning, TurbineFuel}
import net.bdew.lib.recipes.gencfg.GenericConfigLoader
import net.bdew.lib.recipes.{RecipeLoader, RecipeStatement}
import net.minecraftforge.fluids.FluidRegistry

class Loader extends RecipeLoader with GenericConfigLoader {
  val cfgStore = Tuning

  def getFluid(s: String) =
    Option(FluidRegistry.getFluid(s)).getOrElse(error("Fluid %s not found", s))

  override def newParser() = new Parser
  override def processRecipeStatement(st: RecipeStatement) = st match {

    case RsTurbineFuel(fluid, value) =>
      Generators.logInfo("Adding turbine fuel %s at %f MJ/mB", fluid, value)
      TurbineFuel.addFuel(getFluid(fluid), value)

    case RsTurbineBlacklist(fluid) =>
      Generators.logInfo("Blacklisting turbine fuel %s")
      TurbineFuel.removeFuel(getFluid(fluid))

    case _ => super.processRecipeStatement(st)
  }
}
