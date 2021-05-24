package net.bdew.generators.modules.forgeOutput

import net.bdew.generators.registries.{Blocks, Machines}
import net.bdew.lib.Text
import net.bdew.lib.multiblock.item.ModuleBlockItem
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.ItemStack
import net.minecraft.util.text.ITextComponent
import net.minecraft.world.World

class BlockItemForgeOutput(block: BlockForgeOutput) extends ModuleBlockItem(block, Blocks.defaultItemProps, Machines) {
  override def getTooltip(stack: ItemStack, world: World, flags: ITooltipFlag): List[ITextComponent] =
    Text.translate("advgenerators.tooltip.fe_output") +:
      super.getTooltip(stack, world, flags)
}
