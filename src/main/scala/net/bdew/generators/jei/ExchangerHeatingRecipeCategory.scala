package net.bdew.generators.jei

import com.mojang.blaze3d.vertex.PoseStack
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.gui.drawable.{IDrawable, IDrawableStatic}
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.category.IRecipeCategory
import mezz.jei.api.registration.IRecipeRegistration
import net.bdew.generators.Generators
import net.bdew.generators.recipes.ExchangerRecipeHeating
import net.bdew.generators.registries.{Machines, Recipes}
import net.bdew.lib.gui.Color
import net.bdew.lib.recipes.{MixedIngredient, RecipeReloadListener, ResourceOutput}
import net.bdew.lib.{Client, Text}
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraftforge.fluids.FluidStack

import scala.jdk.CollectionConverters._

object ExchangerHeatingRecipeCategory extends IRecipeCategory[ExchangerRecipeHeating] {
  override def getUid: ResourceLocation = new ResourceLocation(Generators.ModId, "exchanger_heating")

  val fluidOverlay: IDrawableStatic = JEIPlugin.guiHelper.createDrawable(
    new ResourceLocation(Generators.ModId, "textures/gui/widgets.png"),
    10, 0, 9, 58
  )

  override def getRecipeClass: Class[_ <: ExchangerRecipeHeating] = classOf[ExchangerRecipeHeating]

  override def getTitle: Component = Text.translate("advgenerators.recipe.exchanger.heating")

  override def getBackground: IDrawable =
    JEIPlugin.guiHelper.drawableBuilder(
      new ResourceLocation(Generators.ModId, "textures/gui/exchanger.png"),
      7, 17, 65, 62
    ).addPadding(0, 0, 50, 50).build()

  override def getIcon: IDrawable = JEIPlugin.guiHelper.createDrawableIngredient(
    VanillaTypes.ITEM, new ItemStack(Machines.controllerExchanger.item.get()))

  override def setIngredients(recipe: ExchangerRecipeHeating, ingredients: IIngredients): Unit = {
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

  override def setRecipe(recipeLayout: IRecipeLayout, recipe: ExchangerRecipeHeating, ingredients: IIngredients): Unit = {
    val inAmt = recipe.input match {
      case x: MixedIngredient.Fluid =>
        recipeLayout.getFluidStacks.init(0, true, 52, 2, 9, 58, 1000, false, fluidOverlay)
        1000D
      case x: MixedIngredient.Item =>
        recipeLayout.getItemStacks.init(0, true, new ResourceItemRenderer(9, 58, 1, fluidOverlay), 52, 2, 9, 58, 0, 0)
        1D
    }

    val outAmt = inAmt / recipe.inPerHU * recipe.outPerHU

    recipe.output match {
      case x: ResourceOutput.FluidOutput =>
        recipeLayout.getFluidStacks.init(1, false, 104, 2, 9, 58, outAmt.toInt, false, fluidOverlay)
      case x: ResourceOutput.ItemOutput =>
        recipeLayout.getItemStacks.init(1, false, new ResourceItemRenderer(9, 58, outAmt, fluidOverlay), 104, 2, 9, 58, 0, 0)
    }

    recipeLayout.getFluidStacks.set(ingredients)
    recipeLayout.getItemStacks.set(ingredients)
  }

  override def draw(recipe: ExchangerRecipeHeating, matrixStack: PoseStack, mouseX: Double, mouseY: Double): Unit = {
    super.draw(recipe, matrixStack, mouseX, mouseY)
    Client.fontRenderer.draw(matrixStack, Text.amount(1000 / recipe.inPerHU, "hu"), 118, 50, Color.darkGray.asRGB)
  }

  def initRecipes(reg: IRecipeRegistration): Unit = {
    val allRecipes = Recipes.exchangerHeatingType.getAllRecipes(RecipeReloadListener.clientRecipeManager)
    reg.addRecipes(allRecipes.asJava, getUid)
  }
}
