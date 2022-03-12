package net.bdew.generators.registries

import net.bdew.generators.recipes._
import net.bdew.lib.managers.{MachineRecipeDef, RecipeDef, RecipeManager}
import net.bdew.lib.recipes.MachineRecipeType

object Recipes extends RecipeManager {
  val liquidFuel: MachineRecipeDef[LiquidFuelRecipe, LiquidFuelRecipeSerializer] =
    registerMachine("liquid_fuel", () => new LiquidFuelRecipeSerializer)

  val carbonSource: MachineRecipeDef[CarbonSourceRecipe, CarbonSourceRecipeSerializer] =
    registerMachine("carbon_source", () => new CarbonSourceRecipeSerializer)

  val upgrade: MachineRecipeDef[UpgradeRecipe, UpgradeRecipeSerializer] =
    registerMachine("upgrade_kit", () => new UpgradeRecipeSerializer)

  val exchangerHeating: MachineRecipeDef[ExchangerRecipeHeating, ExchangerHeatingRecipeSerializer] =
    registerMachine("exchanger_heating", () => new ExchangerHeatingRecipeSerializer)

  val exchangerCooling: MachineRecipeDef[ExchangerRecipeCooling, ExchangerCoolingRecipeSerializer] =
    registerMachine("exchanger_cooling", () => new ExchangerCoolingRecipeSerializer)
}
