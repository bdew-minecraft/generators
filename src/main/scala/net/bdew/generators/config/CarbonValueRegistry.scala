package net.bdew.generators.config

import net.bdew.generators.recipes.CarbonSourceRecipe
import net.bdew.generators.registries.Recipes
import net.bdew.lib.recipes.RecipeReloadListener
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.RecipeManager

object CarbonValueRegistry {
  var recipes = Set.empty[CarbonSourceRecipe]

  def refreshRecipes(manager: RecipeManager): Unit = {
    recipes = Recipes.carbonSourceType.getAllRecipes(manager).toSet
  }

  def init(): Unit = {
    RecipeReloadListener.onServerRecipeUpdate.listen(refreshRecipes)
    RecipeReloadListener.onClientRecipeUpdate.listen(refreshRecipes)
  }

  def getValueOpt(stack: ItemStack): Option[Float] =
    recipes.find(_.ingredient.test(stack)).map(_.carbonValue)

  def isValid(stack: ItemStack): Boolean =
    recipes.exists(_.ingredient.test(stack))
}
