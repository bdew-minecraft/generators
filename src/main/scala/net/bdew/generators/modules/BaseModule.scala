package net.bdew.generators.modules

import net.bdew.generators.registries.Blocks
import net.bdew.lib.multiblock.ModuleType
import net.bdew.lib.multiblock.block.BlockModule
import net.bdew.lib.multiblock.tile.TileModule

class BaseModule[T <: TileModule](kind: ModuleType) extends BlockModule[T](Blocks.machineProps, kind)
