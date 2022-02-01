package net.bdew.generators.modules.turbine

import net.bdew.generators.registries.{Blocks, Machines}
import net.bdew.lib.Text
import net.bdew.lib.Text.pimpTextComponent
import net.bdew.lib.multiblock.item.ModuleBlockItem
import net.minecraft.network.chat.Component
import net.minecraft.world.item.{ItemStack, TooltipFlag}
import net.minecraft.world.level.Level

class BlockItemTurbine(block: BlockTurbine) extends ModuleBlockItem(block, Blocks.defaultItemProps, Machines) {
  override def getTooltip(stack: ItemStack, world: Level, flags: TooltipFlag): List[Component] =
    Text.translate("advgenerators.tooltip.turbine.produce", Text.energyPerTick(block.cfg.maxFEPerTick()).setColor(Text.Color.YELLOW)) +:
      Text.translate("advgenerators.tooltip.turbine.inertia", Text.string("%.0f%%".format(100D * block.cfg.inertia())).setColor(Text.Color.YELLOW)) +:
      super.getTooltip(stack, world, flags)
}
