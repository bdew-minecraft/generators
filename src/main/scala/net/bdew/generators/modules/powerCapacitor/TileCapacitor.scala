package net.bdew.generators.modules.powerCapacitor

import net.bdew.generators.registries.Modules
import net.bdew.lib.multiblock.ModuleType
import net.bdew.lib.multiblock.tile.TileModule
import net.bdew.lib.tile.TileExtended
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class TileCapacitor(teType: BlockEntityType[_], pos: BlockPos, state: BlockState) extends TileExtended(teType, pos, state) with TileModule {
  override val kind: ModuleType = Modules.powerCapacitor
}
