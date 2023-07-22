package net.bdew.generators.config

import net.bdew.generators.recipes.CarbonSourceRecipe
import net.bdew.generators.registries.Recipes
import net.bdew.lib.recipes.RecipeReloadListener
import net.minecraft.core.RegistryAccess
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeManager

object CarbonValueRegistry {
  var recipes = Set.empty[CarbonSourceRecipe]

  def refreshRecipes(manager: RecipeManager, ra: RegistryAccess): Unit = {
    recipes = Recipes.carbonSource.from(manager).toSet
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
