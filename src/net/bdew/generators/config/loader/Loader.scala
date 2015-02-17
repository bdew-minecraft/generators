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
import net.bdew.generators.config.{ExchangerRegistry, Tuning, TurbineFuel}
import net.bdew.lib.recipes.gencfg.GenericConfigLoader
import net.bdew.lib.recipes.{RecipeLoader, RecipeStatement}
import net.bdew.lib.resource.{FluidResource, ItemResource, Resource}
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.oredict.OreDictionary

class Loader extends RecipeLoader with GenericConfigLoader {
  val cfgStore = Tuning

  def getFluid(s: String) =
    Option(FluidRegistry.getFluid(s)).getOrElse(error("Fluid %s not found", s))

  def resolveResourceKind(x: ResKindRef) = x match {
    case ResKindItem(sr) =>
      val is = getConcreteStack(sr)
      if (is.getItemDamage == OreDictionary.WILDCARD_VALUE) {
        Generators.logInfo("Meta is unset in %s, defaulting to 0", x)
        is.setItemDamage(0)
      }
      ItemResource(is.getItem, is.getItemDamage)
    case ResKindFluid(f) =>
      FluidResource(getFluid(f))
    case _ => error("Unknown resource ref: %s", x)
  }

  def resolveResource(x: ResourceRef) = Resource(resolveResourceKind(x.res), x.amount)

  override def newParser() = new Parser
  override def processRecipeStatement(st: RecipeStatement) = st match {

    case RsTurbineFuel(fluid, value) =>
      Generators.logInfo("Adding turbine fuel %s at %f MJ/mB", fluid, value)
      TurbineFuel.addFuel(getFluid(fluid), value)

    case RsTurbineBlacklist(fluid) =>
      Generators.logInfo("Blacklisting turbine fuel %s", fluid)
      TurbineFuel.removeFuel(getFluid(fluid))

    case RsExchangerHeat(input, output, heat) =>
      val inRes = resolveResource(input)
      val outRes = resolveResource(output)
      Generators.logInfo("Adding exchanger heating %s (%f) -> %s (%f)", inRes.kind, inRes.amount / heat, outRes.kind, outRes.amount / heat)
      ExchangerRegistry.addHeating(inRes.kind, outRes.kind, inRes.amount / heat, outRes.amount / heat)

    case RsExchangerCool(input, output, heat) =>
      val inRes = resolveResource(input)
      val outRes = resolveResource(output)
      Generators.logInfo("Adding exchanger cooling %s (%f) -> %s (%f)", inRes.kind, inRes.amount / heat, outRes.kind, outRes.amount / heat)
      ExchangerRegistry.addCooling(inRes.kind, outRes.kind, inRes.amount / heat, outRes.amount / heat)

    case RsExchangerBlacklist(rk) =>
      val res = resolveResourceKind(rk)
      Generators.logInfo("Blacklisting from exchanger: %s", res)
      ExchangerRegistry.remove(res)

    case _ => super.processRecipeStatement(st)
  }
}
