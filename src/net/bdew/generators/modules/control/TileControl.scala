/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.control

import net.bdew.generators.control._
import net.bdew.lib.data.DataSlotBoolean
import net.bdew.lib.data.base.{DataSlot, UpdateKind}
import net.bdew.lib.multiblock.tile.TileModule
import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class TileControl extends TileModule with MIControl {
  val kind = "Control"
  override def getCore = getCoreAs[CIControl]

  val action = DataSlotControlAction("action", this).setUpdate(UpdateKind.SAVE, UpdateKind.GUI)
  val mode = DataSlotBoolean("mode", this).setUpdate(UpdateKind.SAVE, UpdateKind.GUI)

  def notifyChange(): Unit = {
    getCore.foreach(_.onControlStateChanged())
  }

  override def dataSlotChanged(slot: DataSlot): Unit = {
    if (slot == action || slot == mode) {
      notifyChange()
    }
    super.dataSlotChanged(slot)
  }

  override def getControlState(a: ControlAction): ControlResult.Value =
    if (action :== a)
      ControlResult.fromBool(mode :== (worldObj.isBlockIndirectlyGettingPowered(pos) > 0))
    else
      ControlResult.NEUTRAL

  override def shouldRefresh(world: World, pos: BlockPos, oldState: IBlockState, newSate: IBlockState): Boolean =
    newSate.getBlock != oldState.getBlock
}
