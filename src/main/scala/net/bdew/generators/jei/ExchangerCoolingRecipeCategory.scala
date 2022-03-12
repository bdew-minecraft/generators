package net.bdew.generators.jei

import com.mojang.blaze3d.vertex.PoseStack
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.category.IRecipeCategory
import mezz.jei.api.registration.IRecipeRegistration
import net.bdew.generators.Generators
import net.bdew.generators.recipes.ExchangerRecipeCooling
import net.bdew.generators.registries.{Machines, Recipes}
import net.bdew.lib.gui.Color
import net.bdew.lib.recipes.{MixedIngredient, RecipeReloadListener, ResourceOutput}
import net.bdew.lib.{Client, Text}
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraftforge.fluids.FluidStack

import scala.jdk.CollectionConverters._

object ExchangerCoolingRecipeCategory extends IRecipeCategory[ExchangerRecipeCooling] {
  override def getUid: ResourceLocation = new ResourceLocation(Generators.ModId, "exchanger_cooling")

  override def getRecipeClass: Class[_ <: ExchangerRecipeCooling] = classOf[ExchangerRecipeCooling]

  override def getTitle: Component = Text.translate("advgenerators.recipe.exchanger.cooling")

  override def getBackground: IDrawable =
    JEIPlugin.guiHelper.drawableBuilder(
      new ResourceLocation(Generators.ModId, "textures/gui/exchanger.png"),
      7, 17, 65, 62
    ).addPadding(0, 0, 50, 50).build()

  override def getIcon: IDrawable = JEIPlugin.guiHelper.createDrawableIngredient(
    VanillaTypes.ITEM, new ItemStack(Machines.controllerExchanger.item.get()))

  override def setIngredients(recipe: ExchangerRecipeCooling, ingredients: IIngredients): Unit = {
    val inAmt = recipe.input match {
      case x: MixedIngredient.Item =>
        ingredients.setInputLists(VanillaTypes.ITEM, java.util.Collections.singletonList(
          x.ingredient.getItems.toList.asJava
        ))
        1D
      case x: MixedIngredient.Fluid =>
        ingredients.setInputLists(VanillaTypes.FLUID, java.util.Collections.singletonList(
          x.ingredient.fluids.map(x => new FluidStack(x, 1000)).toList.asJava
        ))
        1000D
    }

    val outAmt = inAmt / recipe.inPerHU * recipe.outPerHU

    recipe.output match {
      case x: ResourceOutput.ItemOutput =>
        ingredients.setOutput(VanillaTypes.ITEM, new ItemStack(x.item))
      case x: ResourceOutput.FluidOutput =>
        ingredients.setOutput(VanillaTypes.FLUID, new FluidStack(x.fluid, outAmt.toInt))
    }
  }

  override def setRecipe(recipeLayout: IRecipeLayout, recipe: ExchangerRecipeCooling, ingredients: IIngredients): Unit = {
    val inAmt = recipe.input match {
      case x: MixedIngredient.Fluid =>
        recipeLayout.getFluidStacks.init(0, true, 64, 2, 37, 16, 1000, false, null)
        1000D
      case x: MixedIngredient.Item =>
        recipeLayout.getItemStacks.init(0, true, new ResourceItemRenderer(37, 16, 1, null), 64, 2, 37, 16, 0, 0)
        1D
    }

    val outAmt = inAmt / recipe.inPerHU * recipe.outPerHU

    recipe.output match {
      case x: ResourceOutput.FluidOutput =>
        recipeLayout.getFluidStacks.init(1, false, 64, 44, 37, 16, outAmt.toInt, false, null)
      case x: ResourceOutput.ItemOutput =>
        recipeLayout.getItemStacks.init(1, false, new ResourceItemRenderer(37, 16, outAmt, null), 64, 44, 37, 16, 0, 0)
    }

    recipeLayout.getFluidStacks.set(ingredients)
    recipeLayout.getItemStacks.set(ingredients)
  }

  override def draw(recipe: ExchangerRecipeCooling, matrixStack: PoseStack, mouseX: Double, mouseY: Double): Unit = {
    super.draw(recipe, matrixStack, mouseX, mouseY)
    Client.fontRenderer.draw(matrixStack, Text.amount(1000 / recipe.inPerHU, "hu"), 118, 50, Color.darkGray.asRGB)
  }

  def initRecipes(reg: IRecipeRegistration): Unit = {
    val allRecipes = Recipes.exchangerCooling.from(RecipeReloadListener.clientRecipeManager)
    reg.addRecipes(allRecipes.asJava, getUid)
  }
}
