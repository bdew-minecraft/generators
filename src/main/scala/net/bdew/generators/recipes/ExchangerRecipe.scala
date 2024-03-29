package net.bdew.generators.recipes

import com.google.gson.{JsonObject, JsonSyntaxException}
import net.bdew.generators.registries.Recipes
import net.bdew.lib.recipes.{BaseMachineRecipe, BaseMachineRecipeSerializer, MixedIngredient, ResourceOutput}
import net.bdew.lib.{JSDouble, JSObj}
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.{RecipeSerializer, RecipeType}

abstract class ExchangerRecipeSerializer[T <: ExchangerRecipe] extends BaseMachineRecipeSerializer[T] {
  def recipeFactory(id: ResourceLocation, input: MixedIngredient, output: ResourceOutput, inPerHU: Double, outPerHU: Double): T

  override def fromJson(recipeId: ResourceLocation, obj: JsonObject): T = {
    (obj.get("input"), obj.get("output"), obj.get("heat")) match {
      case (JSObj(input), JSObj(output), JSDouble(heat)) =>
        recipeFactory(recipeId,
          MixedIngredient.fromJson(input),
          ResourceOutput.fromJson(output),
          input.get("amount").getAsDouble / heat,
          output.get("amount").getAsDouble / heat
        )
      case _ => throw new JsonSyntaxException("Input and output objects required")
    }
  }

  override def fromNetwork(recipeId: ResourceLocation, buff: FriendlyByteBuf): T = {
    recipeFactory(recipeId,
      MixedIngredient.fromPacket(buff),
      ResourceOutput.fromPacket(buff),
      buff.readDouble(),
      buff.readDouble()
    )
  }

  override def toNetwork(buffer: FriendlyByteBuf, recipe: T): Unit = {
    recipe.input.toPacket(buffer)
    recipe.output.toPacket(buffer)
    buffer.writeDouble(recipe.inPerHU)
    buffer.writeDouble(recipe.outPerHU)
  }
}

class ExchangerHeatingRecipeSerializer extends ExchangerRecipeSerializer[ExchangerRecipeHeating] {
  override def recipeFactory(id: ResourceLocation, input: MixedIngredient, output: ResourceOutput, inPerHU: Double, outPerHU: Double): ExchangerRecipeHeating =
    new ExchangerRecipeHeating(id, input, output, inPerHU, outPerHU)
}

class ExchangerCoolingRecipeSerializer extends ExchangerRecipeSerializer[ExchangerRecipeCooling] {
  override def recipeFactory(id: ResourceLocation, input: MixedIngredient, output: ResourceOutput, inPerHU: Double, outPerHU: Double): ExchangerRecipeCooling =
    new ExchangerRecipeCooling(id, input, output, inPerHU, outPerHU)
}

abstract class ExchangerRecipe(id: ResourceLocation, val input: MixedIngredient, val output: ResourceOutput, val inPerHU: Double, val outPerHU: Double) extends BaseMachineRecipe(id)

class ExchangerRecipeHeating(id: ResourceLocation, input: MixedIngredient, output: ResourceOutput, inPerHU: Double, outPerHU: Double) extends ExchangerRecipe(id, input, output, inPerHU, outPerHU) {
  override def getSerializer: RecipeSerializer[_] = Recipes.exchangerHeating.serializer
  override def getType: RecipeType[_] = Recipes.exchangerHeating.recipeType
}

class ExchangerRecipeCooling(id: ResourceLocation, input: MixedIngredient, output: ResourceOutput, inPerHU: Double, outPerHU: Double) extends ExchangerRecipe(id, input, output, inPerHU, outPerHU) {
  override def getSerializer: RecipeSerializer[_] = Recipes.exchangerCooling.serializer
  override def getType: RecipeType[_] = Recipes.exchangerCooling.recipeType
}
