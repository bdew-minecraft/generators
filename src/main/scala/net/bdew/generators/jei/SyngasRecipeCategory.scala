package net.bdew.generators.jei

import com.mojang.blaze3d.vertex.PoseStack
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.gui.drawable.{IDrawable, IDrawableStatic}
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.category.IRecipeCategory
import mezz.jei.api.registration.IRecipeRegistration
import net.bdew.generators.Generators
import net.bdew.generators.config.Config
import net.bdew.generators.recipes.CarbonSourceRecipe
import net.bdew.generators.registries.{Fluids, Machines, Recipes}
import net.bdew.lib.Text
import net.bdew.lib.recipes.RecipeReloadListener
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraftforge.fluids.FluidStack

import java.util
import scala.jdk.CollectionConverters._

object SyngasRecipeCategory extends IRecipeCategory[CarbonSourceRecipe] {
  override def getUid: ResourceLocation = new ResourceLocation(Generators.ModId, "syngas_conversion")

  var maxCarbon = 1000f
  var maxSyngas = 1000

  val fluidOverlay: IDrawableStatic = JEIPlugin.guiHelper.createDrawable(
    new ResourceLocation(Generators.ModId, "textures/gui/widgets.png"),
    137, 54, 10, 35
  )

  val coal: IDrawableStatic = JEIPlugin.guiHelper.drawableBuilder(
    new ResourceLocation("minecraft:textures/block/coal_block.png"), 0, 0, 10, 16)
    .setTextureSize(16, 16).build()

  override def getRecipeClass: Class[_ <: CarbonSourceRecipe] = classOf[CarbonSourceRecipe]

  override def getTitle: Component = Text.translate("advgenerators.recipe.syngas_conversion")

  override def getBackground: IDrawable =
    JEIPlugin.guiHelper.createDrawable(
      new ResourceLocation(Generators.ModId, "textures/gui/jei.png"),
      0, 0, 164, 56
    )

  override def getIcon: IDrawable = JEIPlugin.guiHelper.createDrawableIngredient(
    VanillaTypes.ITEM, new ItemStack(Machines.controllerSyngas.item.get()))

  override def setIngredients(recipe: CarbonSourceRecipe, ingredients: IIngredients): Unit = {
    ingredients.setInputLists[ItemStack](VanillaTypes.ITEM,
      util.Collections.singletonList(recipe.ingredient.getItems.toList.asJava))
    ingredients.setOutput[FluidStack](VanillaTypes.FLUID,
      new FluidStack(Fluids.syngas.source.get(), (recipe.carbonValue / Config.SyngasProducer.carbonPerMBSyngas()).toInt))
  }

  override def setRecipe(recipeLayout: IRecipeLayout, recipe: CarbonSourceRecipe, ingredients: IIngredients): Unit = {
    recipeLayout.getItemStacks.init(0, true, 19, 19)
    recipeLayout.getItemStacks.set(ingredients)
    recipeLayout.getFluidStacks.init(0, false, 131, 11, 10, 35, maxSyngas, false, fluidOverlay)
    recipeLayout.getFluidStacks.set(ingredients)
  }

  override def draw(recipe: CarbonSourceRecipe, matrixStack: PoseStack, mouseX: Double, mouseY: Double): Unit = {
    super.draw(recipe, matrixStack, mouseX, mouseY)
    val fullHeight = 35
    var fillHeight = (fullHeight * recipe.carbonValue / maxCarbon).ceil.toInt
    var pos = 0
    while (fillHeight > 0) {
      if (fillHeight > 16) {
        coal.draw(matrixStack, 77, 11 + fullHeight - pos - 16)
        fillHeight -= 16
        pos += 16
      } else {
        coal.draw(matrixStack, 77, 11 + fullHeight - pos - 16, 16 - fillHeight, 0, 0, 0)
        fillHeight = 0
      }
    }
    fluidOverlay.draw(matrixStack, 77, 11)
  }

  override def getTooltipStrings(recipe: CarbonSourceRecipe, mouseX: Double, mouseY: Double): util.List[Component] = {
    if (mouseX >= 77 && mouseX <= 87 && mouseY >= 11 && mouseY <= 46)
      util.Collections.singletonList(Text.amount(recipe.carbonValue, "carbon"))
    else
      super.getTooltipStrings(recipe, mouseX, mouseY)
  }

  def initRecipes(reg: IRecipeRegistration): Unit = {
    val allRecipes = Recipes.carbonSourceType.getAllRecipes(RecipeReloadListener.clientRecipeManager)
    reg.addRecipes(allRecipes.asJava, getUid)
    maxCarbon = allRecipes.map(_.carbonValue).max
    maxSyngas = (maxCarbon / Config.SyngasProducer.carbonPerMBSyngas()).toInt
  }
}
