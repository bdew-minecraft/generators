package net.bdew.generators.modules.powerCapacitor

import net.bdew.generators.registries.{Blocks, Machines}
import net.bdew.lib.Text
import net.bdew.lib.Text.pimpTextComponent
import net.bdew.lib.multiblock.item.ModuleBlockItem
import net.minecraft.network.chat.Component
import net.minecraft.world.item.{ItemStack, TooltipFlag}
import net.minecraft.world.level.Level

class BlockItemCapacitor(block: BlockCapacitor) extends ModuleBlockItem(block, Blocks.defaultItemProps, Machines) {
  override def getTooltip(stack: ItemStack, world: Level, flags: TooltipFlag): List[Component] =
    Text.translate("advgenerators.tooltip.capacity", Text.energy(block.cfg.capacity()).setColor(Text.Color.YELLOW)) +:
      super.getTooltip(stack, world, flags)
}
