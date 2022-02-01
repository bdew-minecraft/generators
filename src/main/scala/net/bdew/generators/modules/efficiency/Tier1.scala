package net.bdew.generators.modules.efficiency

import net.bdew.generators.config.Config
import net.bdew.generators.modules.BaseModule
import net.bdew.generators.registries.{Blocks, Machines, Modules}
import net.bdew.lib.PimpVanilla._
import net.bdew.lib.Text
import net.bdew.lib.Text.pimpTextComponent
import net.bdew.lib.multiblock.ModuleType
import net.bdew.lib.multiblock.item.ModuleBlockItem
import net.bdew.lib.multiblock.tile.{TileController, TileModule}
import net.bdew.lib.tile.TileExtended
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.item.{ItemStack, TooltipFlag}
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class BlockEfficiencyUpgradeTier1 extends BaseModule[TileEfficiencyUpgradeTier1](Modules.efficiencyUpgradeTier1)

class BlockItemEfficiencyUpgradeTier1(block: BlockEfficiencyUpgradeTier1) extends ModuleBlockItem(block, Blocks.defaultItemProps, Machines) {
  override def getTooltip(stack: ItemStack, world: Level, flags: TooltipFlag): List[Component] =
    Text.translate("advgenerators.tooltip.efficiency", Text.string("%.0f%%".format(Config.FuelTurbine.fuelEfficiencyTier1() * 100)).setColor(Text.Color.YELLOW)) +:
      super.getTooltip(stack, world, flags)
}

class TileEfficiencyUpgradeTier1(teType: BlockEntityType[_], pos: BlockPos, state: BlockState) extends TileExtended(teType, pos, state) with TileModule {
  val kind: ModuleType = Modules.efficiencyUpgradeTier1

  override def connect(target: TileController): Unit = {
    super.connect(target)
    (target.modules.set ++ Set(target.getBlockPos)).flatMap(_.neighbours.values)
      .flatMap(pos => getLevel.getTileSafe[TileEfficiencyUpgradeTier2](pos))
      .filter(_.connected.isEmpty)
      .foreach(_.tryConnect())
  }
}

