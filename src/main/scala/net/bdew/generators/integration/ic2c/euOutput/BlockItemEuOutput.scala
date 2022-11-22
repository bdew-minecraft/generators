package net.bdew.generators.integration.ic2c.euOutput

import net.bdew.generators.registries.{Blocks, Machines}
import net.bdew.lib.multiblock.item.ModuleBlockItem
import net.bdew.lib.{DecFormat, Text}
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.{ItemStack, TooltipFlag}
import net.minecraft.world.level.Level

class BlockItemEuOutput(block: BlockEuOutput) extends ModuleBlockItem(block, Blocks.defaultItemProps, Machines) {
  override def getTooltip(stack: ItemStack, world: Level, flags: TooltipFlag): List[Component] =
    Text.translate("advgenerators.tooltip.eu",
      Text.string(block.tier.number.toString).withStyle(ChatFormatting.YELLOW),
      Text.string(DecFormat.thousandsFmt.format(block.tier.maxPower)).withStyle(ChatFormatting.YELLOW)
    ) +: super.getTooltip(stack, world, flags)
}
