/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.fluidOutputSelect

import net.bdew.generators.modules.BaseModule
import net.bdew.lib.multiblock.block.BlockOutput
import net.minecraft.util.{BlockPos, EnumFacing}
import net.minecraft.world.IBlockAccess

object BlockFluidOutputSelect extends BaseModule("FluidOutputSelect", "FluidOutputSelect", classOf[TileFluidOutputSelect]) with BlockOutput[TileFluidOutputSelect] {
  override def canConnectRedstone(world: IBlockAccess, pos: BlockPos, side: EnumFacing): Boolean = true
}
