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
import net.bdew.generators.config.Config
import net.bdew.generators.registries.{Fluids, Machines}
import net.bdew.lib.Text
import net.bdew.lib.misc.Taggable
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.material.Fluid
import net.minecraftforge.fluids.FluidStack

import java.util
import java.util.Collections
import scala.jdk.CollectionConverters._

case class TurbineSteamRecipe(fe: Double)

object TurbineSteamRecipeCategory extends IRecipeCategory[TurbineSteamRecipe] {
  override val getRecipeType: RecipeType[TurbineSteamRecipe] =
    RecipeType.create(Generators.ModId, "turbine_steam", classOf[TurbineSteamRecipe])

  val fluidOverlay: IDrawableStatic = JEIPlugin.guiHelper.createDrawable(
    new ResourceLocation(Generators.ModId, "textures/gui/widgets.png"),
    10, 0, 9, 58
  )

  val powerFill: IDrawableStatic = JEIPlugin.guiHelper.createDrawable(
    new ResourceLocation(Generators.ModId, "textures/gui/widgets.png"),
    0, 0, 9, 58
  )

  override def getTitle: Component = Text.translate("advgenerators.recipe.turbine_steam")

  override def getBackground: IDrawable =
    JEIPlugin.guiHelper.drawableBuilder(
      new ResourceLocation(Generators.ModId, "textures/gui/turbine.png"),
      7, 17, 65, 62
    ).addPadding(0, 0, 50, 50).build()

  override def getIcon: IDrawable = JEIPlugin.guiHelper.createDrawableIngredient(
    VanillaTypes.ITEM_STACK, new ItemStack(Machines.controllerSteamTurbine.item.get()))

  override def setRecipe(builder: IRecipeLayoutBuilder, recipe: TurbineSteamRecipe, focuses: IFocusGroup): Unit = {
    builder.addSlot(RecipeIngredientRole.INPUT, 52, 2)
      .addIngredients(ForgeTypes.FLUID_STACK, Taggable[Fluid].resolve(Fluids.steamTag).toList.map(x => new FluidStack(x, 1000)).asJava)
      .setFluidRenderer(1000, false, 9, 58)
      .setOverlay(fluidOverlay, 0, 0)
  }


  override def draw(recipe: TurbineSteamRecipe, recipeSlotsView: IRecipeSlotsView, guiGraphics: GuiGraphics, mouseX: Double, mouseY: Double): Unit = {
    powerFill.draw(guiGraphics, 104, 2)
  }

  override def getTooltipStrings(recipe: TurbineSteamRecipe, recipeSlotsView: IRecipeSlotsView, mouseX: Double, mouseY: Double): util.List[Component] = {
    if (mouseX >= 104 && mouseX <= 113 && mouseY >= 2 && mouseY <= 60)
      util.Collections.singletonList(Text.energy(recipe.fe))
    else
      Collections.emptyList
  }

  def initRecipes(reg: IRecipeRegistration): Unit = {
    reg.addRecipes(getRecipeType, Collections.singletonList(TurbineSteamRecipe(Config.SteamTurbine.steamEnergyDensity() * 1000)))
  }
}
