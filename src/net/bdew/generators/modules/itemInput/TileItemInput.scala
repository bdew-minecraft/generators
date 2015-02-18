/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.itemInput

import net.bdew.lib.multiblock.interact.CIItemInput
import net.bdew.lib.multiblock.tile.TileModule
import net.bdew.lib.tile.inventory.InventoryProxy
import net.minecraft.inventory.ISidedInventory
import net.minecraft.item.ItemStack

class TileItemInput extends TileModule with InventoryProxy with ISidedInventory {
  val kind = "ItemInput"

  override def getCore = getCoreAs[CIItemInput]
  override def targetInventory = getCore map (_.getItemInputInventory)

  override def getAccessibleSlotsFromSide(side: Int) =
    (for {
      core <- getCore.toList // because for comprehension can't have lists after options... *facepalm*
      slot <- 0 until core.getItemInputInventory.getSizeInventory
      if core.canInputItemToSlot(slot)
    } yield slot).toArray

  override def canExtractItem(slot: Int, item: ItemStack, side: Int) = false
  override def canInsertItem(slot: Int, item: ItemStack, side: Int) =
    getCore.exists(c => c.canInputItemToSlot(slot) && c.getItemInputInventory.isItemValidForSlot(slot, item))
}
