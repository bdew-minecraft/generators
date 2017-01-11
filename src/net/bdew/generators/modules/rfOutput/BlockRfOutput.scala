/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.rfOutput

import net.bdew.generators.modules.BaseModule
import net.bdew.lib.multiblock.block.BlockOutput
import net.minecraft.block.state.IBlockState
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.{IBlockAccess, World}

object BlockRfOutput extends BaseModule("RFOutput", "PowerOutput", classOf[TileRfOutput]) with BlockOutput[TileRfOutput] {
  override def canConnectRedstone(state: IBlockState, world: IBlockAccess, pos: BlockPos, side: EnumFacing): Boolean = true

  override def rotateBlock(world: World, pos: BlockPos, axis: EnumFacing): Boolean = {
    val te = getTE(world, pos)
    if (!world.isRemote && te.getCore.isDefined) {
      te.forcedSides(axis) := !te.forcedSides(axis)
      te.doRescanFaces()
      true
    } else false
  }
}