package net.bdew.generators.recipes

import com.google.gson.JsonObject
import net.bdew.generators.registries.Recipes
import net.bdew.lib.recipes.{BaseMachineRecipe, BaseMachineRecipeSerializer}
import net.minecraft.item.crafting.{IRecipeSerializer, IRecipeType, Ingredient}
import net.minecraft.network.PacketBuffer
import net.minecraft.util.ResourceLocation

class CarbonSourceRecipeSerializer extends BaseMachineRecipeSerializer[CarbonSourceRecipe] {
  override def fromJson(recipeId: ResourceLocation, obj: JsonObject): CarbonSourceRecipe = {
    val ingr = Ingredient.fromJson(obj.get("items"))
    val carbonValue = obj.get("carbonValue").getAsFloat
    new CarbonSourceRecipe(recipeId, ingr, carbonValue)
  }

  override def fromNetwork(recipeId: ResourceLocation, buff: PacketBuffer): CarbonSourceRecipe = {
    val ingr = Ingredient.fromNetwork(buff)
    val carbonValue = buff.readFloat()
    new CarbonSourceRecipe(recipeId, ingr, carbonValue)
  }

  override def toNetwork(buffer: PacketBuffer, recipe: CarbonSourceRecipe): Unit = {
    recipe.ingredient.toNetwork(buffer)
    buffer.writeFloat(recipe.carbonValue)
  }
}

class CarbonSourceRecipe(id: ResourceLocation, val ingredient: Ingredient, val carbonValue: Float) extends BaseMachineRecipe(id) {
  override def getSerializer: IRecipeSerializer[_] = Recipes.carbonSourceSerializer.get()
  override def getType: IRecipeType[_] = Recipes.carbonSourceType
}
