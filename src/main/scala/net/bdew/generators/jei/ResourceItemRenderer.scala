package net.bdew.generators.jei

import com.mojang.blaze3d.vertex.PoseStack
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.ingredients.IIngredientRenderer
import net.bdew.lib.gui.{Color, Rect, Texture}
import net.bdew.lib.{Client, DecFormat, Text}
import net.minecraft.network.chat.Component
import net.minecraft.world.item.{ItemStack, TooltipFlag}

import java.util

class ResourceItemRenderer(width: Int, height: Int, amount: Double, overlay: IDrawable) extends IIngredientRenderer[ItemStack] {
  override def getWidth: Int = width
  override def getHeight: Int = height

  override def render(stack: PoseStack, ingredient: ItemStack): Unit = {
    JEIPlugin.drawTarget.drawTextureTiled(
      stack, Rect(0, 0, width, height),
      Texture.block(Client.minecraft.getItemRenderer.getItemModelShaper.getItemModel(ingredient).getParticleIcon),
      16, 16, Color.fromInt(Client.minecraft.getItemColors.getColor(ingredient, 0))
    )
    if (overlay != null)
      overlay.draw(stack, 0, 0)
  }

  override def getTooltip(ingredient: ItemStack, flag: TooltipFlag): util.List[Component] = {
    val list = ingredient.getTooltipLines(Client.player, flag)
    list.set(0, Text.string(DecFormat.short(amount) + " ").append(list.get(0)))
    list
  }
}
