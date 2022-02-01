package net.bdew.generators.config

import net.bdew.generators.recipes.ExchangerRecipe
import net.bdew.generators.registries.Recipes
import net.bdew.lib.recipes.RecipeReloadListener
import net.bdew.lib.resource.Resource
import net.minecraft.world.item.crafting.RecipeManager

object ExchangerRecipeRegistry {
  var coolers = Set.empty[ExchangerRecipe]
  var heaters = Set.empty[ExchangerRecipe]

  def refreshRecipes(manager: RecipeManager): Unit = {
    coolers = Recipes.exchangerHeatingType.getAllRecipes(manager).toSet
    heaters = Recipes.exchangerCoolingType.getAllRecipes(manager).toSet
  }

  def init(): Unit = {
    RecipeReloadListener.onServerRecipeUpdate.listen(refreshRecipes)
    RecipeReloadListener.onClientRecipeUpdate.listen(refreshRecipes)
  }

  def findHeater(res: Resource): Option[ExchangerRecipe] =
    heaters.find(_.input.matches(res))

  def findCooler(res: Resource): Option[ExchangerRecipe] =
    coolers.find(_.input.matches(res))

  def isValidHeater(res: Resource): Boolean =
    heaters.exists(_.input.matches(res))

  def isValidCooler(res: Resource): Boolean =
    coolers.exists(_.input.matches(res))
}
