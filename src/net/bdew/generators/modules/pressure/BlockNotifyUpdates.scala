/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license
 */

package net.bdew.generators.modules.pressure

import net.bdew.pressure.api.PressureAPI
import net.minecraft.block.Block
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

trait BlockNotifyUpdates extends Block {
  def notifyPressureSystemUpdate(w: World, x: Int, y: Int, z: Int) =
    PressureAPI.HELPER.notifyBlockChanged(w, x, y, z)

  override def breakBlock(world: World, x: Int, y: Int, z: Int, block: Block, meta: Int) = {
    notifyPressureSystemUpdate(world, x, y, z)
    super.breakBlock(world, x, y, z, block, meta)
  }

  override def onBlockPlaced(world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float, meta: Int) = {
    notifyPressureSystemUpdate(world, x, y, z)
    super.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, meta)
  }

  override def rotateBlock(world: World, x: Int, y: Int, z: Int, axis: ForgeDirection) = {
    if (super.rotateBlock(world, x, y, z, axis)) {
      notifyPressureSystemUpdate(world, x, y, z)
      true
    } else false
  }
}
