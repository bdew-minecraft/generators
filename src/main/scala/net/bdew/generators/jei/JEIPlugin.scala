package net.bdew.generators.jei

import mezz.jei.api.helpers.{IGuiHelper, IJeiHelpers}
import mezz.jei.api.registration.{IRecipeCatalystRegistration, IRecipeCategoryRegistration, IRecipeRegistration}
import mezz.jei.api.{IModPlugin, JeiPlugin}
import net.bdew.generators.Generators
import net.bdew.generators.registries.{Items, Machines}
import net.bdew.lib.Client
import net.bdew.lib.gui.{DrawTarget, SimpleDrawTarget}
import net.minecraft.client.gui.Font
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack


@JeiPlugin
class JEIPlugin extends IModPlugin {
  override def getPluginUid: ResourceLocation = new ResourceLocation(Generators.ModId, "jei")

  override def registerCategories(registration: IRecipeCategoryRegistration): Unit = {
    JEIPlugin.helpers = registration.getJeiHelpers
    registration.addRecipeCategories(
      TurbineFuelRecipeCategory,
      TurbineSteamRecipeCategory,
      SyngasRecipeCategory,
      UpgradeRecipeCategory,
      ExchangerHeatingRecipeCategory,
      ExchangerCoolingRecipeCategory,
    )
  }

  override def registerRecipes(registration: IRecipeRegistration): Unit = {
    TurbineFuelRecipeCategory.initRecipes(registration)
    TurbineSteamRecipeCategory.initRecipes(registration)
    SyngasRecipeCategory.initRecipes(registration)
    UpgradeRecipeCategory.initRecipes(registration)
    ExchangerHeatingRecipeCategory.initRecipes(registration)
    ExchangerCoolingRecipeCategory.initRecipes(registration)
  }

  override def registerRecipeCatalysts(registration: IRecipeCatalystRegistration): Unit = {
    registration.addRecipeCatalyst(new ItemStack(Machines.controllerFuelTurbine.item.get()), TurbineFuelRecipeCategory.getUid)
    registration.addRecipeCatalyst(new ItemStack(Machines.controllerSteamTurbine.item.get()), TurbineSteamRecipeCategory.getUid)
    registration.addRecipeCatalyst(new ItemStack(Machines.controllerSyngas.item.get()), SyngasRecipeCategory.getUid)
    registration.addRecipeCatalyst(new ItemStack(Items.upgradeKit.get()), UpgradeRecipeCategory.getUid)
    registration.addRecipeCatalyst(new ItemStack(Machines.controllerExchanger.item.get()), ExchangerHeatingRecipeCategory.getUid)
    registration.addRecipeCatalyst(new ItemStack(Machines.controllerExchanger.item.get()), ExchangerCoolingRecipeCategory.getUid)
  }
}

object JEIPlugin {
  var helpers: IJeiHelpers = _
  def guiHelper: IGuiHelper = helpers.getGuiHelper
  val drawTarget: DrawTarget = new SimpleDrawTarget {
    override def getZLevel: Float = 0
    override def getFontRenderer: Font = Client.fontRenderer
  }
}