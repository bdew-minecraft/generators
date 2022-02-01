package net.bdew.generators.modules.fuelTank

import net.bdew.generators.registries.{Blocks, Machines}
import net.bdew.lib.Text
import net.bdew.lib.multiblock.item.ModuleBlockItem
import net.minecraft.network.chat.Component
import net.minecraft.world.item.{ItemStack, TooltipFlag}
import net.minecraft.world.level.Level

class BlockItemFuelTank(block: BlockFuelTank) extends ModuleBlockItem(block, Blocks.defaultItemProps, Machines) {
  override def getTooltip(stack: ItemStack, world: Level, flags: TooltipFlag): List[Component] =
    Text.translate("advgenerators.tooltip.capacity", Text.fluid(block.cfg.capacity())) +:
      super.getTooltip(stack, world, flags)
}
