package net.bdew.generators.registries

import net.bdew.generators.recipes._
import net.bdew.lib.managers.RegistryManager
import net.bdew.lib.recipes.MachineRecipeType
import net.minecraftforge.registries.{ForgeRegistries, RegistryObject}

object Recipes extends RegistryManager(ForgeRegistries.RECIPE_SERIALIZERS) {
  val liquidFuelSerializer: RegistryObject[LiquidFuelRecipeSerializer] = register("liquid_fuel", () => new LiquidFuelRecipeSerializer)
  val liquidFuelType: MachineRecipeType[LiquidFuelRecipe] = new MachineRecipeType(liquidFuelSerializer)

  val carbonSourceSerializer: RegistryObject[CarbonSourceRecipeSerializer] = register("carbon_source", () => new CarbonSourceRecipeSerializer)
  val carbonSourceType: MachineRecipeType[CarbonSourceRecipe] = new MachineRecipeType(carbonSourceSerializer)

  val upgradeSerializer: RegistryObject[UpgradeRecipeSerializer] = register("upgrade_kit", () => new UpgradeRecipeSerializer)
  val upgradeType: MachineRecipeType[UpgradeRecipe] = new MachineRecipeType(upgradeSerializer)

  val exchangerHeatingSerializer: RegistryObject[ExchangerHeatingRecipeSerializer] =
    register("exchanger_heating", () => new ExchangerHeatingRecipeSerializer)
  val exchangerHeatingType: MachineRecipeType[ExchangerRecipeHeating] = new MachineRecipeType(exchangerHeatingSerializer)

  val exchangerCoolingSerializer: RegistryObject[ExchangerCoolingRecipeSerializer] =
    register("exchanger_cooling", () => new ExchangerCoolingRecipeSerializer)
  val exchangerCoolingType: MachineRecipeType[ExchangerRecipeCooling] = new MachineRecipeType(exchangerCoolingSerializer)
}
