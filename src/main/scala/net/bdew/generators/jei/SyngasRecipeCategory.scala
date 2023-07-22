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
import net.bdew.generators.recipes.CarbonSourceRecipe
import net.bdew.generators.registries.{Fluids, Machines, Recipes}
import net.bdew.lib.Text
import net.bdew.lib.recipes.RecipeReloadListener
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraftforge.fluids.FluidStack

import java.util
import java.util.Collections
import scala.jdk.CollectionConverters._

object SyngasRecipeCategory extends IRecipeCategory[CarbonSourceRecipe] {
  override val getRecipeType: RecipeType[CarbonSourceRecipe] =
    RecipeType.create(Generators.ModId, "syngas_conversion", classOf[CarbonSourceRecipe])

  var maxCarbon = 1000f
  var maxSyngas = 1000

  val fluidOverlay: IDrawableStatic = JEIPlugin.guiHelper.createDrawable(
    new ResourceLocation(Generators.ModId, "textures/gui/widgets.png"),
    137, 54, 10, 35
  )

  val coal: IDrawableStatic = JEIPlugin.guiHelper.drawableBuilder(
    new ResourceLocation("minecraft:textures/block/coal_block.png"), 0, 0, 10, 16)
    .setTextureSize(16, 16).build()


  override def getTitle: Component = Text.translate("advgenerators.recipe.syngas_conversion")

  override def getBackground: IDrawable =
    JEIPlugin.guiHelper.createDrawable(
      new ResourceLocation(Generators.ModId, "textures/gui/jei.png"),
      0, 0, 164, 56
    )

  override def getIcon: IDrawable = JEIPlugin.guiHelper.createDrawableIngredient(
    VanillaTypes.ITEM_STACK, new ItemStack(Machines.controllerSyngas.item.get()))

  override def setRecipe(builder: IRecipeLayoutBuilder, recipe: CarbonSourceRecipe, focuses: IFocusGroup): Unit = {
    builder.addSlot(RecipeIngredientRole.INPUT, 20, 20)
      .addIngredients(recipe.ingredient)
    builder.addSlot(RecipeIngredientRole.OUTPUT, 131, 11)
      .addIngredient(ForgeTypes.FLUID_STACK, new FluidStack(Fluids.syngas.source.get(), (recipe.carbonValue / Config.SyngasProducer.carbonPerMBSyngas()).toInt))
      .setFluidRenderer(maxSyngas, false, 10, 35)
      .setOverlay(fluidOverlay, 0, 0)
  }


  override def draw(recipe: CarbonSourceRecipe, recipeSlotsView: IRecipeSlotsView, guiGraphics: GuiGraphics, mouseX: Double, mouseY: Double): Unit = {
    val fullHeight = 35
    var fillHeight = (fullHeight * recipe.carbonValue / maxCarbon).ceil.toInt
    var pos = 0
    while (fillHeight > 0) {
      if (fillHeight > 16) {
        coal.draw(guiGraphics, 77, 11 + fullHeight - pos - 16)
        fillHeight -= 16
        pos += 16
      } else {
        coal.draw(guiGraphics, 77, 11 + fullHeight - pos - 16, 16 - fillHeight, 0, 0, 0)
        fillHeight = 0
      }
    }
    fluidOverlay.draw(guiGraphics, 77, 11)
  }

  override def getTooltipStrings(recipe: CarbonSourceRecipe, recipeSlotsView: IRecipeSlotsView, mouseX: Double, mouseY: Double): util.List[Component] = {
    if (mouseX >= 77 && mouseX <= 87 && mouseY >= 11 && mouseY <= 46)
      util.Collections.singletonList(Text.amount(recipe.carbonValue, "carbon"))
    else
      Collections.emptyList
  }

  def initRecipes(reg: IRecipeRegistration): Unit = {
    val allRecipes = Recipes.carbonSource.from(RecipeReloadListener.clientRecipeManager)
    reg.addRecipes(getRecipeType, allRecipes.asJava)
    maxCarbon = allRecipes.map(_.carbonValue).max
    maxSyngas = (maxCarbon / Config.SyngasProducer.carbonPerMBSyngas()).toInt
  }
}
