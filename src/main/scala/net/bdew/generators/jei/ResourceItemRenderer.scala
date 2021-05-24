package net.bdew.generators.jei

import com.mojang.blaze3d.matrix.MatrixStack
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.ingredients.IIngredientRenderer
import net.bdew.lib.gui.{Color, Rect, Texture}
import net.bdew.lib.{Client, DecFormat, Text}
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.ItemStack
import net.minecraft.util.text.ITextComponent

import java.util

class ResourceItemRenderer(width: Int, height: Int, amount: Double, overlay: IDrawable) extends IIngredientRenderer[ItemStack] {
  override def render(matrixStack: MatrixStack, xPosition: Int, yPosition: Int, ingredient: ItemStack): Unit = {
    JEIPlugin.drawTarget.drawTextureTiled(
      matrixStack, Rect(xPosition, yPosition, width, height),
      Texture.block(Client.minecraft.getItemRenderer.getItemModelShaper.getParticleIcon(ingredient)),
      16, 16, Color.fromInt(Client.minecraft.getItemColors.getColor(ingredient, 0))
    )
    if (overlay != null)
      overlay.draw(matrixStack, xPosition, yPosition)
  }

  override def getTooltip(ingredient: ItemStack, flag: ITooltipFlag): util.List[ITextComponent] = {
    val list = ingredient.getTooltipLines(Client.player, flag)
    list.set(0, Text.string(DecFormat.short(amount) + " ").append(list.get(0)))
    list
  }
}
