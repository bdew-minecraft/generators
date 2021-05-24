package net.bdew.generators.modules.turbine

import net.bdew.generators.registries.{Blocks, Machines}
import net.bdew.lib.Text
import net.bdew.lib.Text.pimpTextComponent
import net.bdew.lib.multiblock.item.ModuleBlockItem
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.ItemStack
import net.minecraft.util.text.ITextComponent
import net.minecraft.world.World

class BlockItemTurbine(block: BlockTurbine) extends ModuleBlockItem(block, Blocks.defaultItemProps, Machines) {
  override def getTooltip(stack: ItemStack, world: World, flags: ITooltipFlag): List[ITextComponent] =
    Text.translate("advgenerators.tooltip.turbine.produce", Text.energyPerTick(block.cfg.maxFEPerTick()).setColor(Text.Color.YELLOW)) +:
      Text.translate("advgenerators.tooltip.turbine.inertia", Text.string("%.0f%%".format(100D * block.cfg.inertia())).setColor(Text.Color.YELLOW)) +:
      super.getTooltip(stack, world, flags)
}
