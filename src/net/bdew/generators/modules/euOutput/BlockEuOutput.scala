/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.euOutput

import net.bdew.generators.modules.BaseModule
import net.bdew.lib.multiblock.block.BlockOutput
import net.bdew.lib.rotate.BlockFacingMeta
import net.minecraft.block.state.IBlockState
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.{IBlockAccess, World}

class BlockEuOutputBase[T <: TileEuOutputBase](name: String, texture: String, TEClass: Class[T]) extends BaseModule(name, "PowerOutput", TEClass) with BlockOutput[T] with BlockFacingMeta {

  override def getDefaultFacing = EnumFacing.SOUTH

  override def setFacing(world: World, pos: BlockPos, facing: EnumFacing): Unit = {
    super.setFacing(world, pos, facing)
    getTE(world, pos).tryConnect()
  }

  override def canConnectRedstone(state: IBlockState, world: IBlockAccess, pos: BlockPos, side: EnumFacing): Boolean = true
}

object BlockEuOutputLV extends BlockEuOutputBase("EuOutputLV", "lv", classOf[TileEuOutputLV])

object BlockEuOutputMV extends BlockEuOutputBase("EuOutputMV", "mv", classOf[TileEuOutputMV])

object BlockEuOutputHV extends BlockEuOutputBase("EuOutputHV", "hv", classOf[TileEuOutputHV])

object BlockEuOutputEV extends BlockEuOutputBase("EuOutputEV", "ev", classOf[TileEuOutputEV])