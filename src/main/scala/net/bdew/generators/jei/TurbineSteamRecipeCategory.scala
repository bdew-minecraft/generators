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
import net.bdew.generators.registries.Machines
import net.bdew.lib.Text
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.FluidTags
import net.minecraft.world.item.ItemStack
import net.minecraftforge.fluids.FluidStack

import java.util
import scala.jdk.CollectionConverters._

case class TurbineSteamRecipe(fe: Double)

object TurbineSteamRecipeCategory extends IRecipeCategory[TurbineSteamRecipe] {
  override def getUid: ResourceLocation = new ResourceLocation(Generators.ModId, "turbine_steam")

  val fluidOverlay: IDrawableStatic = JEIPlugin.guiHelper.createDrawable(
    new ResourceLocation(Generators.ModId, "textures/gui/widgets.png"),
    10, 0, 9, 58
  )

  val powerFill: IDrawableStatic = JEIPlugin.guiHelper.createDrawable(
    new ResourceLocation(Generators.ModId, "textures/gui/widgets.png"),
    0, 0, 9, 58
  )

  override def getRecipeClass: Class[_ <: TurbineSteamRecipe] = classOf[TurbineSteamRecipe]

  override def getTitle: Component = Text.translate("advgenerators.recipe.turbine_steam")

  override def getBackground: IDrawable =
    JEIPlugin.guiHelper.drawableBuilder(
      new ResourceLocation(Generators.ModId, "textures/gui/turbine.png"),
      7, 17, 65, 62
    ).addPadding(0, 0, 50, 50).build()

  override def getIcon: IDrawable = JEIPlugin.guiHelper.createDrawableIngredient(
    VanillaTypes.ITEM, new ItemStack(Machines.controllerSteamTurbine.item.get()))

  override def setIngredients(recipe: TurbineSteamRecipe, ingredients: IIngredients): Unit = {
    ingredients.setInputLists[FluidStack](VanillaTypes.FLUID,
      util.Collections.singletonList(
        FluidTags.getAllTags.getTag(new ResourceLocation("forge:steam"))
          .getValues.asScala.map(f => new FluidStack(f, 1000)).asJava
      )
    )
  }

  override def setRecipe(recipeLayout: IRecipeLayout, recipe: TurbineSteamRecipe, ingredients: IIngredients): Unit = {
    recipeLayout.getFluidStacks.init(0, true, 52, 2, 9, 58, 1000, false, fluidOverlay)
    recipeLayout.getFluidStacks.set(ingredients)
  }

  override def draw(recipe: TurbineSteamRecipe, matrixStack: PoseStack, mouseX: Double, mouseY: Double): Unit = {
    super.draw(recipe, matrixStack, mouseX, mouseY)
    powerFill.draw(matrixStack, 104, 2)
  }

  override def getTooltipStrings(recipe: TurbineSteamRecipe, mouseX: Double, mouseY: Double): util.List[Component] = {
    if (mouseX >= 104 && mouseX <= 113 && mouseY >= 2 && mouseY <= 60)
      util.Collections.singletonList(Text.energy(recipe.fe))
    else
      super.getTooltipStrings(recipe, mouseX, mouseY)
  }

  def initRecipes(reg: IRecipeRegistration): Unit = {
    reg.addRecipes(util.Collections.singletonList(TurbineSteamRecipe(Config.SteamTurbine.steamEnergyDensity() * 1000)), getUid)
  }
}
