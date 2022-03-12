package net.bdew.generators.jei

import com.mojang.blaze3d.vertex.PoseStack
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.gui.drawable.{IDrawable, IDrawableStatic}
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.category.IRecipeCategory
import mezz.jei.api.registration.IRecipeRegistration
import net.bdew.generators.Generators
import net.bdew.generators.recipes.LiquidFuelRecipe
import net.bdew.generators.registries.{Machines, Recipes}
import net.bdew.lib.Text
import net.bdew.lib.recipes.RecipeReloadListener
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraftforge.fluids.FluidStack

import java.util
import scala.jdk.CollectionConverters._

object TurbineFuelRecipeCategory extends IRecipeCategory[LiquidFuelRecipe] {
  override def getUid: ResourceLocation = new ResourceLocation(Generators.ModId, "turbine_fuels")

  var maxPower = 1000000f

  val fluidOverlay: IDrawableStatic = JEIPlugin.guiHelper.createDrawable(
    new ResourceLocation(Generators.ModId, "textures/gui/widgets.png"),
    10, 0, 9, 58
  )

  val powerFill: IDrawableStatic = JEIPlugin.guiHelper.createDrawable(
    new ResourceLocation(Generators.ModId, "textures/gui/widgets.png"),
    0, 0, 9, 58
  )

  override def getRecipeClass: Class[_ <: LiquidFuelRecipe] = classOf[LiquidFuelRecipe]

  override def getTitle: Component = Text.translate("advgenerators.recipe.turbine_fuels")

  override def getBackground: IDrawable =
    JEIPlugin.guiHelper.drawableBuilder(
      new ResourceLocation(Generators.ModId, "textures/gui/turbine.png"),
      7, 17, 65, 62
    ).addPadding(0, 0, 50, 50).build()

  override def getIcon: IDrawable = JEIPlugin.guiHelper.createDrawableIngredient(
    VanillaTypes.ITEM, new ItemStack(Machines.controllerFuelTurbine.item.get()))

  override def setIngredients(recipe: LiquidFuelRecipe, ingredients: IIngredients): Unit = {
    ingredients.setInputLists[FluidStack](VanillaTypes.FLUID, util.Collections.singletonList(
      recipe.input.fluids.toList.map(x => new FluidStack(x, 1000)).asJava
    ))
  }

  override def setRecipe(recipeLayout: IRecipeLayout, recipe: LiquidFuelRecipe, ingredients: IIngredients): Unit = {
    recipeLayout.getFluidStacks.init(0, true, 52, 2, 9, 58, 1000, false, fluidOverlay)
    recipeLayout.getFluidStacks.set(ingredients)
  }

  override def draw(recipe: LiquidFuelRecipe, matrixStack: PoseStack, mouseX: Double, mouseY: Double): Unit = {
    super.draw(recipe, matrixStack, mouseX, mouseY)
    powerFill.draw(matrixStack, 104, 2, ((1 - (recipe.fePerMb * 1000 / maxPower)) * 58).floor.toInt, 0, 0, 0)
  }

  override def getTooltipStrings(recipe: LiquidFuelRecipe, mouseX: Double, mouseY: Double): util.List[Component] = {
    if (mouseX >= 104 && mouseX <= 113 && mouseY >= 2 && mouseY <= 60)
      util.Collections.singletonList(Text.energy(recipe.fePerMb * 1000))
    else
      super.getTooltipStrings(recipe, mouseX, mouseY)
  }

  def initRecipes(reg: IRecipeRegistration): Unit = {
    val allRecipes = Recipes.liquidFuel.from(RecipeReloadListener.clientRecipeManager)
    reg.addRecipes(allRecipes.asJava, getUid)
    maxPower = allRecipes.map(_.fePerMb * 1000).max
  }
}
