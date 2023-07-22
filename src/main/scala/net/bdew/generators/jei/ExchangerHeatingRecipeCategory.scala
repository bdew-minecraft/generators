package net.bdew.generators.jei

import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.forge.ForgeTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.{IDrawable, IDrawableStatic}
import mezz.jei.api.gui.ingredient.IRecipeSlotsView
import mezz.jei.api.recipe.category.IRecipeCategory
import mezz.jei.api.recipe.{IFocusGroup, RecipeIngredientRole, RecipeType}
import mezz.jei.api.registration.IRecipeRegistration
import net.bdew.generators.Generators
import net.bdew.generators.recipes.ExchangerRecipeHeating
import net.bdew.generators.registries.{Machines, Recipes}
import net.bdew.lib.gui.Color
import net.bdew.lib.recipes.{MixedIngredient, RecipeReloadListener, ResourceOutput}
import net.bdew.lib.{Client, Text}
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraftforge.fluids.FluidStack

import scala.jdk.CollectionConverters._

object ExchangerHeatingRecipeCategory extends IRecipeCategory[ExchangerRecipeHeating] {
  override val getRecipeType: RecipeType[ExchangerRecipeHeating] =
    RecipeType.create(Generators.ModId, "exchanger_heating", classOf[ExchangerRecipeHeating])

  val fluidOverlay: IDrawableStatic = JEIPlugin.guiHelper.createDrawable(
    new ResourceLocation(Generators.ModId, "textures/gui/widgets.png"),
    10, 0, 9, 58
  )

  override def getTitle: Component = Text.translate("advgenerators.recipe.exchanger.heating")

  override def getBackground: IDrawable =
    JEIPlugin.guiHelper.drawableBuilder(
      new ResourceLocation(Generators.ModId, "textures/gui/exchanger.png"),
      7, 17, 65, 62
    ).addPadding(0, 0, 50, 50).build()

  override def getIcon: IDrawable = JEIPlugin.guiHelper.createDrawableIngredient(
    VanillaTypes.ITEM_STACK, new ItemStack(Machines.controllerExchanger.item.get()))

  def inputAmount(recipe: ExchangerRecipeHeating): Double = {
    recipe.input match {
      case MixedIngredient.Fluid(_) => 1000D
      case MixedIngredient.Item(_) => 1D
    }
  }

  override def setRecipe(builder: IRecipeLayoutBuilder, recipe: ExchangerRecipeHeating, focuses: IFocusGroup): Unit = {
    recipe.input match {
      case MixedIngredient.Fluid(ingredient) =>
        builder.addSlot(RecipeIngredientRole.INPUT, 52, 2)
          .addIngredients(ForgeTypes.FLUID_STACK, ingredient.fluids.map(f => new FluidStack(f, 1000)).toList.asJava)
          .setFluidRenderer(1000, false, 9, 58)
          .setOverlay(fluidOverlay, 0, 0)
      case MixedIngredient.Item(ingredient) =>
        builder.addSlot(RecipeIngredientRole.INPUT, 52, 2)
          .addIngredients(ingredient)
          .setCustomRenderer(VanillaTypes.ITEM_STACK, new ResourceItemRenderer(9, 58, 1, null))
          .setOverlay(fluidOverlay, 0, 0)
    }

    val outAmt = inputAmount(recipe) / recipe.inPerHU * recipe.outPerHU

    recipe.output match {
      case ResourceOutput.FluidOutput(fluid) =>
        builder.addSlot(RecipeIngredientRole.OUTPUT, 104, 2)
          .addIngredient(ForgeTypes.FLUID_STACK, new FluidStack(fluid, outAmt.toInt))
          .setFluidRenderer(1000, false, 9, 58)
          .setOverlay(fluidOverlay, 0, 0)
      case ResourceOutput.ItemOutput(item) =>
        builder.addSlot(RecipeIngredientRole.OUTPUT, 104, 2)
          .addItemStack(new ItemStack(item))
          .setCustomRenderer(VanillaTypes.ITEM_STACK, new ResourceItemRenderer(9, 58, outAmt, null))
          .setOverlay(fluidOverlay, 0, 0)
    }
  }


  override def draw(recipe: ExchangerRecipeHeating, recipeSlotsView: IRecipeSlotsView, guiGraphics: GuiGraphics, mouseX: Double, mouseY: Double): Unit = {
    guiGraphics.drawString(Client.fontRenderer, Text.amount(1000 / recipe.inPerHU, "hu"), 118, 50, Color.darkGray.asRGB)
  }

  def initRecipes(reg: IRecipeRegistration): Unit = {
    val allRecipes = Recipes.exchangerHeating.from(RecipeReloadListener.clientRecipeManager)
    reg.addRecipes(getRecipeType, allRecipes.asJava)
  }
}
