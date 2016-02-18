/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.itemOutput

import net.bdew.generators.compat.itempush.ItemPush
import net.bdew.generators.config.Tuning
import net.bdew.lib.multiblock.data.OutputConfigItems
import net.bdew.lib.multiblock.interact.CIItemOutput
import net.bdew.lib.multiblock.tile.{RSControllableOutput, TileOutput}
import net.bdew.lib.tile.inventory.InventoryProxy
import net.minecraft.inventory.ISidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing

class TileItemOutput extends TileOutput[OutputConfigItems] with RSControllableOutput with InventoryProxy with ISidedInventory {
  val kind = "ItemOutput"

  override def targetInventory = getCoreAs[CIItemOutput] map (_.getItemOutputInventory)

  override val outputConfigType = classOf[OutputConfigItems]
  override def makeCfgObject(face: EnumFacing) = new OutputConfigItems

  val ratio = Tuning.getSection("Power").getFloat("RF_MJ_Ratio")

  override def canConnectToFace(d: EnumFacing) = ItemPush.isValidTarget(this, d)

  override def doOutput(face: EnumFacing, cfg: OutputConfigItems): Unit = {
    for {
      core <- getCoreAs[CIItemOutput] if checkCanOutput(cfg)
      slot <- 0 until core.getItemOutputInventory.getSizeInventory
      orig <- Option(core.getItemOutputInventory.getStackInSlot(slot))
    } {
      val stack = orig.copy()
      val left = ItemPush.pushStack(this, face, stack)
      if (left == null)
        core.getItemOutputInventory.decrStackSize(slot, orig.stackSize)
      else
        core.getItemOutputInventory.decrStackSize(slot, orig.stackSize - left.stackSize)
    }
  }

  override def getSlotsForFace(side: EnumFacing): Array[Int] =
    targetInventory map (x => (0 until x.getSizeInventory).toArray) getOrElse Array.empty
  override def canExtractItem(slot: Int, item: ItemStack, side: EnumFacing) =
    getCfg(side) exists checkCanOutput
  override def canInsertItem(slot: Int, item: ItemStack, side: EnumFacing) = false
}
