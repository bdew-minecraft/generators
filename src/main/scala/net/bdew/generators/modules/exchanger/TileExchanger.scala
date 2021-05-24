package net.bdew.generators.modules.exchanger

import net.bdew.generators.registries.Modules
import net.bdew.lib.multiblock.ModuleType
import net.bdew.lib.multiblock.tile.TileModule
import net.bdew.lib.tile.TileExtended
import net.minecraft.tileentity.TileEntityType

class TileExchanger(teType: TileEntityType[_]) extends TileExtended(teType) with TileModule {
  val kind: ModuleType = Modules.heatExchanger
}
