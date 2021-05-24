package net.bdew.generators.modules.gasInput

import mekanism.api.Action
import mekanism.api.chemical.gas.GasStack
import mekanism.api.inventory.IgnoredIInventory
import mekanism.api.recipes.RotaryRecipe
import net.minecraft.item.crafting.IRecipeType
import net.minecraft.util.ResourceLocation
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction

import scala.jdk.CollectionConverters._

object GasConversion {
  val rotaryRecipeType: IRecipeType[RotaryRecipe] =
    Registry.RECIPE_TYPE.get(new ResourceLocation("mekanism", "rotary")).asInstanceOf[IRecipeType[RotaryRecipe]]

  def getRecipes(world: World): List[RotaryRecipe] =
    world.getRecipeManager.getAllRecipesFor[IgnoredIInventory, RotaryRecipe](rotaryRecipeType).asScala.toList

  def convert(gas: GasStack, world: World): Option[FluidStack] = {
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

  def convert(fluid: FluidStack, world: World): Option[GasStack] = {
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
