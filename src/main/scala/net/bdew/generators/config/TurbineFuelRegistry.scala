package net.bdew.generators.config

import net.bdew.generators.recipes.LiquidFuelRecipe
import net.bdew.generators.registries.Recipes
import net.bdew.lib.recipes.RecipeReloadListener
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.level.material.Fluid
import net.minecraftforge.fluids.FluidStack

object TurbineFuelRegistry {
  var recipes = Set.empty[LiquidFuelRecipe]

  def refreshRecipes(manager: RecipeManager): Unit = {
    recipes = Recipes.liquidFuelType.getAllRecipes(manager).toSet
  }

  def init(): Unit = {
    RecipeReloadListener.onServerRecipeUpdate.listen(refreshRecipes)
    RecipeReloadListener.onClientRecipeUpdate.listen(refreshRecipes)
  }

  def isValidFuel(fs: FluidStack): Boolean = recipes.exists(_.input.matches(fs))
  def isValidFuel(f: Fluid): Boolean = recipes.exists(_.input.matches(f))
  def getFuelValue(f: Fluid): Float = recipes.find(_.input.matches(f)).map(_.fePerMb).getOrElse(0)
}
