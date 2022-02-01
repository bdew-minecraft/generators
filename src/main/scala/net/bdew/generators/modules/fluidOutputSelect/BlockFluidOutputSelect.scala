package net.bdew.generators.modules.fluidOutputSelect

import net.bdew.generators.modules.BaseModule
import net.bdew.generators.registries.Modules
import net.bdew.lib.multiblock.block.BlockOutput
import net.bdew.lib.multiblock.misc.BlockForcedOutput
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.BlockState

class BlockFluidOutputSelect extends BaseModule[TileFluidOutputSelect](Modules.fluidOutput) with BlockOutput[TileFluidOutputSelect] with BlockForcedOutput {
  override def canConnectRedstone(state: BlockState, world: BlockGetter, pos: BlockPos, side: Direction): Boolean = true
}
