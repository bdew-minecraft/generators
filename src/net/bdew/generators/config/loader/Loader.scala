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
import net.bdew.generators.config.{CarbonValueRegistry, ExchangerRegistry, Tuning, TurbineFuel}
import net.bdew.lib.recipes.gencfg.GenericConfigLoader
import net.bdew.lib.recipes.{RecipeLoader, RecipeStatement}
import net.bdew.lib.resource.{FluidResource, ItemResource, Resource}
import net.minecraft.tileentity.TileEntityFurnace
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
        Generators.logDebug("Meta is unset in %s, defaulting to 0", x)
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
      if (FluidRegistry.isFluidRegistered(fluid)) {
        Generators.logDebug("Adding turbine fuel %s at %f MJ/mB", fluid, value)
        TurbineFuel.addFuel(getFluid(fluid), value)
      } else {
        Generators.logDebug("Skipping turbine fuel %s - not registered", fluid)
      }

    case RsTurbineBlacklist(fluid) =>
      Generators.logDebug("Blacklisting turbine fuel %s", fluid)
      TurbineFuel.removeFuel(getFluid(fluid))

    case RsExchangerHeat(input, output, heat) =>
      val inRes = resolveResource(input)
      val outRes = resolveResource(output)
      Generators.logDebug("Adding exchanger heating %s (%f) -> %s (%f)", inRes.kind, inRes.amount / heat, outRes.kind, outRes.amount / heat)
      ExchangerRegistry.addHeating(inRes.kind, outRes.kind, inRes.amount / heat, outRes.amount / heat)

    case RsExchangerCool(input, output, heat) =>
      val inRes = resolveResource(input)
      val outRes = resolveResource(output)
      Generators.logDebug("Adding exchanger cooling %s (%f) -> %s (%f)", inRes.kind, inRes.amount / heat, outRes.kind, outRes.amount / heat)
      ExchangerRegistry.addCooling(inRes.kind, outRes.kind, inRes.amount / heat, outRes.amount / heat)

    case RsExchangerBlacklist(rk) =>
      val res = resolveResourceKind(rk)
      Generators.logDebug("Blacklisting from exchanger: %s", res)
      ExchangerRegistry.remove(res)

    case RsCarbonValue(spec, cVal) =>
      Generators.logDebug("Processing carbon value %s => %s", spec, cVal)
      for (stack <- getAllConcreteStacks(spec)) {
        (cVal match {
          case CarbonValueSpecified(value) =>
            Generators.logDebug("Adding carbon value: %s -> %d", stack, value)
            Some(value)
          case CarbonValueDefault() =>
            val value = TileEntityFurnace.getItemBurnTime(stack)
            if (value > 0) {
              Generators.logDebug("Adding carbon value: %s -> %d (from burn time)", stack, value)
              Some(value)
            } else {
              Generators.logWarn("No burn time for %s, skipping", stack)
              None
            }
          case CarbonValueBlacklist() =>
            Generators.logDebug("Blacklist carbon value: %s", stack)
            Some(0)
        }) foreach (CarbonValueRegistry.register(stack, _))
      }

    case RsSetContainer(item, container) =>
      Generators.logDebug("Setting container %s => %s", item, container)
      val target = getConcreteStack(container).getItem
      for (stack <- getAllConcreteStacks(item)) {
        stack.getItem.setContainerItem(target)
      }

    case _ => super.processRecipeStatement(st)
  }
}
