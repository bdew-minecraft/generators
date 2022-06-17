package net.bdew.generators.jei

import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.recipe.category.IRecipeCategory
import mezz.jei.api.recipe.{IFocusGroup, RecipeIngredientRole, RecipeType}
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
  override val getRecipeType: RecipeType[UpgradeRecipe] =
    RecipeType.create(Generators.ModId, "upgrade", classOf[UpgradeRecipe])

  override def getTitle: Component = Text.translate("advgenerators.recipe.upgrade")

  override def getBackground: IDrawable =
    JEIPlugin.guiHelper.drawableBuilder(
      new ResourceLocation(Generators.ModId, "textures/gui/jei.png"),
      0, 57, 125, 18
    ).addPadding(10, 10, 10, 10).build()

  override def getIcon: IDrawable = JEIPlugin.guiHelper.createDrawableIngredient(
    VanillaTypes.ITEM_STACK, new ItemStack(Items.upgradeKit.get()))


  override def setRecipe(builder: IRecipeLayoutBuilder, recipe: UpgradeRecipe, focuses: IFocusGroup): Unit = {
    builder.addSlot(RecipeIngredientRole.INPUT, 11, 11)
      .addItemStack(new ItemStack(recipe.from))
    builder.addSlot(RecipeIngredientRole.INPUT, 60, 11)
      .addItemStack(new ItemStack(recipe.item))
    builder.addSlot(RecipeIngredientRole.OUTPUT, 118, 11)
      .addItemStack(new ItemStack(recipe.to))
  }

  def initRecipes(reg: IRecipeRegistration): Unit = {
    val allRecipes = Recipes.upgrade.from(RecipeReloadListener.clientRecipeManager)
    reg.addRecipes(getRecipeType, allRecipes.asJava)
  }
}
