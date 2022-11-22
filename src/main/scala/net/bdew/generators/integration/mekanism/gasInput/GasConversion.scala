package net.bdew.generators.integration.mekanism.gasInput

import mekanism.api.Action
import mekanism.api.chemical.gas.GasStack
import mekanism.api.inventory.IgnoredIInventory
import mekanism.api.recipes.RotaryRecipe
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction

import scala.jdk.CollectionConverters._

object GasConversion {
  val rotaryRecipeType: RecipeType[RotaryRecipe] =
    Registry.RECIPE_TYPE.get(new ResourceLocation("mekanism", "rotary")).asInstanceOf[RecipeType[RotaryRecipe]]

  def getRecipes(world: Level): List[RotaryRecipe] =
    world.getRecipeManager.getAllRecipesFor[IgnoredIInventory, RotaryRecipe](rotaryRecipeType).asScala.toList

  def convert(gas: GasStack, world: Level): Option[FluidStack] = {
    getRecipes(world)
      .find(rec => rec.test(gas))
      .map(rec => {
        val res = rec.getFluidOutput(gas)
        if (gas.getAmount > Int.MaxValue)
          res.setAmount(Int.MaxValue)
        else {
          res.setAmount(gas.getAmount.toInt)
        }
        res
      })
  }

  def convert(fluid: FluidStack, world: Level): Option[GasStack] = {
    getRecipes(world)
      .find(rec => rec.test(fluid))
      .map(rec => {
        val res = rec.getGasOutput(fluid)
        res.setAmount(fluid.getAmount)
        res
      })
  }

  def convertAction(action: Action): FluidAction =
    if (action == Action.EXECUTE) FluidAction.EXECUTE else FluidAction.SIMULATE
}
