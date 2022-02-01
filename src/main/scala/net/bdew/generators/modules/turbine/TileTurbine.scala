package net.bdew.generators.modules.turbine

import net.bdew.generators.registries.Modules
import net.bdew.lib.multiblock.ModuleType
import net.bdew.lib.multiblock.tile.TileModule
import net.bdew.lib.tile.TileExtended
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class TileTurbine(teType: BlockEntityType[_], pos: BlockPos, state: BlockState) extends TileExtended(teType, pos, state) with TileModule {
  val kind: ModuleType = Modules.turbine
}