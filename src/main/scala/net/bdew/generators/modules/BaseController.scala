package net.bdew.generators.modules

import net.bdew.generators.registries.Blocks
import net.bdew.lib.multiblock.block.BlockController
import net.bdew.lib.multiblock.tile.TileController

class BaseController[T <: TileController] extends BlockController[T](Blocks.machineProps)