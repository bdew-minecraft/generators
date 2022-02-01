package net.bdew.generators.recipes

import com.google.gson.JsonObject
import net.bdew.generators.registries.Recipes
import net.bdew.lib.multiblock.block.BlockModule
import net.bdew.lib.multiblock.tile.TileModule
import net.bdew.lib.recipes.{BaseMachineRecipe, BaseMachineRecipeSerializer}
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.{RecipeSerializer, RecipeType}
import net.minecraft.world.level.block.Block
import net.minecraftforge.registries.ForgeRegistries

class UpgradeRecipeSerializer extends BaseMachineRecipeSerializer[UpgradeRecipe] {
  override def fromJson(recipeId: ResourceLocation, obj: JsonObject): UpgradeRecipe = {
    val item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(obj.get("item").getAsString))
    val from = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(obj.get("from").getAsString)).asInstanceOf[BlockModule[_ <: TileModule]]
    val to = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(obj.get("to").getAsString)).asInstanceOf[BlockModule[_ <: TileModule]]
    new UpgradeRecipe(recipeId, item, from, to)
  }

  override def fromNetwork(recipeId: ResourceLocation, buff: FriendlyByteBuf): UpgradeRecipe = {
    val item = buff.readRegistryId[Item]()
    val from = buff.readRegistryId[Block]().asInstanceOf[BlockModule[_ <: TileModule]]
    val to = buff.readRegistryId[Block]().asInstanceOf[BlockModule[_ <: TileModule]]
    new UpgradeRecipe(recipeId, item, from, to)
  }

  override def toNetwork(buffer: FriendlyByteBuf, recipe: UpgradeRecipe): Unit = {
    buffer.writeRegistryId(recipe.item)
    buffer.writeRegistryId[Block](recipe.from)
    buffer.writeRegistryId[Block](recipe.to)
  }
}

class UpgradeRecipe(id: ResourceLocation, val item: Item, val from: BlockModule[_ <: TileModule], val to: BlockModule[_ <: TileModule]) extends BaseMachineRecipe(id) {
  override def getSerializer: RecipeSerializer[_] = Recipes.upgradeSerializer.get()
  override def getType: RecipeType[_] = Recipes.upgradeType
}
