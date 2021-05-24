package net.bdew.generators.modules.powerCapacitor

import net.bdew.generators.registries.Modules
import net.bdew.lib.multiblock.ModuleType
import net.bdew.lib.multiblock.tile.TileModule
import net.bdew.lib.tile.TileExtended
import net.minecraft.tileentity.TileEntityType

class TileCapacitor(teType: TileEntityType[_]) extends TileExtended(teType) with TileModule {
  override val kind: ModuleType = Modules.powerCapacitor
}
