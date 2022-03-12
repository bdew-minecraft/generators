package net.bdew.generators.recipes

import com.google.gson.JsonObject
import net.bdew.generators.registries.Recipes
import net.bdew.lib.recipes.{BaseMachineRecipe, BaseMachineRecipeSerializer}
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.{Ingredient, RecipeSerializer, RecipeType}

class CarbonSourceRecipeSerializer extends BaseMachineRecipeSerializer[CarbonSourceRecipe] {
  override def fromJson(recipeId: ResourceLocation, obj: JsonObject): CarbonSourceRecipe = {
    val ingr = Ingredient.fromJson(obj.get("items"))
    val carbonValue = obj.get("carbonValue").getAsFloat
    new CarbonSourceRecipe(recipeId, ingr, carbonValue)
  }

  override def fromNetwork(recipeId: ResourceLocation, buff: FriendlyByteBuf): CarbonSourceRecipe = {
    val ingr = Ingredient.fromNetwork(buff)
    val carbonValue = buff.readFloat()
    new CarbonSourceRecipe(recipeId, ingr, carbonValue)
  }

  override def toNetwork(buffer: FriendlyByteBuf, recipe: CarbonSourceRecipe): Unit = {
    recipe.ingredient.toNetwork(buffer)
    buffer.writeFloat(recipe.carbonValue)
  }
}

class CarbonSourceRecipe(id: ResourceLocation, val ingredient: Ingredient, val carbonValue: Float) extends BaseMachineRecipe(id) {
  override def getSerializer: RecipeSerializer[_] = Recipes.carbonSource.serializer
  override def getType: RecipeType[_] = Recipes.carbonSource.recipeType
}
