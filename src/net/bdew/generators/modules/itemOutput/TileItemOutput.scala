/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.itemOutput

import net.bdew.generators.config.Tuning
import net.bdew.lib.capabilities.Capabilities
import net.bdew.lib.capabilities.helpers.ItemHelper
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

  override def canConnectToFace(d: EnumFacing) = ItemHelper.hasItemHandler(world, pos.offset(d), d.getOpposite)

  override def doOutput(face: EnumFacing, cfg: OutputConfigItems): Unit = {
    if (getWorld.getTotalWorldTime % 20 == 0) {
      for (target <- ItemHelper.getItemHandler(world, pos.offset(face), face.getOpposite))
        ItemHelper.pushItems(getCapability(Capabilities.CAP_ITEM_HANDLER, face), target)
    }
  }

  override def getSlotsForFace(side: EnumFacing): Array[Int] =
    targetInventory map (x => (0 until x.getSizeInventory).toArray) getOrElse Array.empty
  override def canExtractItem(slot: Int, item: ItemStack, side: EnumFacing) =
    getCfg(side) exists checkCanOutput
  override def canInsertItem(slot: Int, item: ItemStack, side: EnumFacing) = false
}
