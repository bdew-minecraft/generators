package net.bdew.generators.jei

import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.category.IRecipeCategory
import mezz.jei.api.registration.IRecipeRegistration
import net.bdew.generators.Generators
import net.bdew.generators.recipes.UpgradeRecipe
import net.bdew.generators.registries.{Items, Recipes}
import net.bdew.lib.Text
import net.bdew.lib.recipes.RecipeReloadListener
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

import scala.jdk.CollectionConverters._

object UpgradeRecipeCategory extends IRecipeCategory[UpgradeRecipe] {
  override def getUid: ResourceLocation = new ResourceLocation(Generators.ModId, "upgrade")

  override def getRecipeClass: Class[_ <: UpgradeRecipe] = classOf[UpgradeRecipe]

  override def getTitle: Component = Text.translate("advgenerators.recipe.upgrade")

  override def getBackground: IDrawable =
    JEIPlugin.guiHelper.drawableBuilder(
      new ResourceLocation(Generators.ModId, "textures/gui/jei.png"),
      0, 57, 125, 18
    ).addPadding(10, 10, 10, 10).build()

  override def getIcon: IDrawable = JEIPlugin.guiHelper.createDrawableIngredient(
    VanillaTypes.ITEM, new ItemStack(Items.upgradeKit.get()))

  override def setIngredients(recipe: UpgradeRecipe, ingredients: IIngredients): Unit = {
    ingredients.setInputs[ItemStack](VanillaTypes.ITEM, List(new ItemStack(recipe.from), new ItemStack(recipe.item)).asJava)
    ingredients.setOutput[ItemStack](VanillaTypes.ITEM, new ItemStack(recipe.to))
  }

  override def setRecipe(recipeLayout: IRecipeLayout, recipe: UpgradeRecipe, ingredients: IIngredients): Unit = {
    recipeLayout.getItemStacks.init(0, true, 10, 10)
    recipeLayout.getItemStacks.init(1, true, 59, 10)
    recipeLayout.getItemStacks.init(2, false, 117, 10)
    recipeLayout.getItemStacks.set(ingredients)
  }


  def initRecipes(reg: IRecipeRegistration): Unit = {
    val allRecipes = Recipes.upgrade.from(RecipeReloadListener.clientRecipeManager)
    reg.addRecipes(allRecipes.asJava, getUid)
  }
}
