package net.bdew.generators.jei

import com.mojang.blaze3d.vertex.PoseStack
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.{IDrawable, IDrawableStatic}
import mezz.jei.api.gui.ingredient.IRecipeSlotsView
import mezz.jei.api.recipe.category.IRecipeCategory
import mezz.jei.api.recipe.{IFocusGroup, RecipeIngredientRole, RecipeType}
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
import java.util.Collections
import scala.jdk.CollectionConverters._

object TurbineFuelRecipeCategory extends IRecipeCategory[LiquidFuelRecipe] {
  override val getRecipeType: RecipeType[LiquidFuelRecipe] =
    RecipeType.create(Generators.ModId, "turbine_fuels", classOf[LiquidFuelRecipe])

  @Deprecated override def getUid: ResourceLocation = getRecipeType.getUid
  @Deprecated override def getRecipeClass: Class[_ <: LiquidFuelRecipe] = getRecipeType.getRecipeClass

  var maxPower = 1000000f

  val fluidOverlay: IDrawableStatic = JEIPlugin.guiHelper.createDrawable(
    new ResourceLocation(Generators.ModId, "textures/gui/widgets.png"),
    10, 0, 9, 58
  )

  val powerFill: IDrawableStatic = JEIPlugin.guiHelper.createDrawable(
    new ResourceLocation(Generators.ModId, "textures/gui/widgets.png"),
    0, 0, 9, 58
  )

  override def getTitle: Component = Text.translate("advgenerators.recipe.turbine_fuels")

  override def getBackground: IDrawable =
    JEIPlugin.guiHelper.drawableBuilder(
      new ResourceLocation(Generators.ModId, "textures/gui/turbine.png"),
      7, 17, 65, 62
    ).addPadding(0, 0, 50, 50).build()

  override def getIcon: IDrawable = JEIPlugin.guiHelper.createDrawableIngredient(
    VanillaTypes.ITEM, new ItemStack(Machines.controllerFuelTurbine.item.get()))

  override def setRecipe(builder: IRecipeLayoutBuilder, recipe: LiquidFuelRecipe, focuses: IFocusGroup): Unit = {
    builder.addSlot(RecipeIngredientRole.INPUT, 52, 2)
      .addIngredients(VanillaTypes.FLUID, recipe.input.fluids.toList.map(x => new FluidStack(x, 1000)).asJava)
      .setFluidRenderer(1000, false, 9, 58)
      .setOverlay(fluidOverlay, 0, 0)
  }

  override def draw(recipe: LiquidFuelRecipe, recipeSlotsView: IRecipeSlotsView, stack: PoseStack, mouseX: Double, mouseY: Double): Unit = {
    powerFill.draw(stack, 104, 2, ((1 - (recipe.fePerMb * 1000 / maxPower)) * 58).floor.toInt, 0, 0, 0)
  }

  override def getTooltipStrings(recipe: LiquidFuelRecipe, recipeSlotsView: IRecipeSlotsView, mouseX: Double, mouseY: Double): util.List[Component] = {
    if (mouseX >= 104 && mouseX <= 113 && mouseY >= 2 && mouseY <= 60)
      util.Collections.singletonList(Text.energy(recipe.fePerMb * 1000))
    else
      Collections.emptyList
  }

  def initRecipes(reg: IRecipeRegistration): Unit = {
    val allRecipes = Recipes.liquidFuel.from(RecipeReloadListener.clientRecipeManager)
    reg.addRecipes(getRecipeType, allRecipes.asJava)
    maxPower = allRecipes.map(_.fePerMb * 1000).max
  }
}
