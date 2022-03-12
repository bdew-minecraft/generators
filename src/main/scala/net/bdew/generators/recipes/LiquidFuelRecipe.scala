package net.bdew.generators.recipes

import com.google.gson.JsonObject
import net.bdew.generators.registries.Recipes
import net.bdew.lib.recipes.{BaseMachineRecipe, BaseMachineRecipeSerializer, FluidIngredient}
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.{RecipeSerializer, RecipeType}

class LiquidFuelRecipeSerializer extends BaseMachineRecipeSerializer[LiquidFuelRecipe] {
  override def fromJson(recipeId: ResourceLocation, obj: JsonObject): LiquidFuelRecipe = {
    val fluid = FluidIngredient.fromJson(obj.get("fuel"))
    val fePerMb = obj.get("fePerMb").getAsFloat
    new LiquidFuelRecipe(recipeId, fluid, fePerMb)
  }

  override def fromNetwork(recipeId: ResourceLocation, buff: FriendlyByteBuf): LiquidFuelRecipe = {
    val fluid = FluidIngredient.fromPacket(buff)
    val fePerMb = buff.readFloat()
    new LiquidFuelRecipe(recipeId, fluid, fePerMb)
  }

  override def toNetwork(buffer: FriendlyByteBuf, recipe: LiquidFuelRecipe): Unit = {
    recipe.input.toPacket(buffer)
    buffer.writeFloat(recipe.fePerMb)
  }
}

class LiquidFuelRecipe(id: ResourceLocation, val input: FluidIngredient, val fePerMb: Float) extends BaseMachineRecipe(id) {
  override def getSerializer: RecipeSerializer[_] = Recipes.liquidFuel.serializer
  override def getType: RecipeType[_] = Recipes.liquidFuel.recipeType
}
