package net.bdew.generators.jei

import com.mojang.blaze3d.vertex.PoseStack
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.forge.ForgeTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.ingredient.IRecipeSlotsView
import mezz.jei.api.recipe.category.IRecipeCategory
import mezz.jei.api.recipe.{IFocusGroup, RecipeIngredientRole, RecipeType}
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
  override val getRecipeType: RecipeType[ExchangerRecipeCooling] =
    RecipeType.create(Generators.ModId, "exchanger_cooling", classOf[ExchangerRecipeCooling])

  override def getTitle: Component = Text.translate("advgenerators.recipe.exchanger.cooling")

  override def getBackground: IDrawable =
    JEIPlugin.guiHelper.drawableBuilder(
      new ResourceLocation(Generators.ModId, "textures/gui/exchanger.png"),
      7, 17, 65, 62
    ).addPadding(0, 0, 50, 50).build()

  override def getIcon: IDrawable = JEIPlugin.guiHelper.createDrawableIngredient(
    VanillaTypes.ITEM_STACK, new ItemStack(Machines.controllerExchanger.item.get()))

  def inputAmount(recipe: ExchangerRecipeCooling): Double = {
    recipe.input match {
      case MixedIngredient.Fluid(_) => 1000D
      case MixedIngredient.Item(_) => 1D
    }
  }

  override def setRecipe(builder: IRecipeLayoutBuilder, recipe: ExchangerRecipeCooling, focuses: IFocusGroup): Unit = {
    recipe.input match {
      case MixedIngredient.Fluid(ingredient) =>
        builder.addSlot(RecipeIngredientRole.INPUT, 64, 2)
          .addIngredients(ForgeTypes.FLUID_STACK, ingredient.fluids.map(f => new FluidStack(f, 1000)).toList.asJava)
          .setFluidRenderer(1000, false, 37, 16)
      case MixedIngredient.Item(ingredient) =>
        builder.addSlot(RecipeIngredientRole.INPUT, 64, 2)
          .addIngredients(ingredient)
          .setCustomRenderer(VanillaTypes.ITEM_STACK, new ResourceItemRenderer(37, 16, 1, null))
    }

    val outAmt = inputAmount(recipe) / recipe.inPerHU * recipe.outPerHU

    recipe.output match {
      case ResourceOutput.FluidOutput(fluid) =>
        builder.addSlot(RecipeIngredientRole.OUTPUT, 64, 44)
          .addIngredient(ForgeTypes.FLUID_STACK, new FluidStack(fluid, outAmt.toInt))
          .setFluidRenderer(1000, false, 37, 16)
      case ResourceOutput.ItemOutput(item) =>
        builder.addSlot(RecipeIngredientRole.OUTPUT, 64, 44)
          .addItemStack(new ItemStack(item))
          .setCustomRenderer(VanillaTypes.ITEM_STACK, new ResourceItemRenderer(37, 16, outAmt, null))
    }
  }

  override def draw(recipe: ExchangerRecipeCooling, recipeSlotsView: IRecipeSlotsView, stack: PoseStack, mouseX: Double, mouseY: Double): Unit = {
    Client.fontRenderer.draw(stack, Text.amount(1000 / recipe.inPerHU, "hu"), 118, 50, Color.darkGray.asRGB)
  }

  def initRecipes(reg: IRecipeRegistration): Unit = {
    val allRecipes = Recipes.exchangerCooling.from(RecipeReloadListener.clientRecipeManager)
    reg.addRecipes(getRecipeType, allRecipes.asJava)
  }
}
