/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.control

import net.bdew.lib.Misc
import net.bdew.lib.data.DataSlotBoolean
import net.bdew.lib.gui.SlotClickable
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.{ClickType, IInventory, Slot}

class SlotControlAction(inv: IInventory, index: Int, x: Int, y: Int, ds: DataSlotControlAction, types: => Seq[ControlAction]) extends Slot(inv, index, x, y) with SlotClickable {
  override def onClick(clickType: ClickType, button: Int, player: EntityPlayer) = {
    if (clickType == ClickType.PICKUP && (button == 0 || button == 1) && types.nonEmpty && player.inventory.getItemStack.isEmpty) {
      if (button == 0)
        ds := Misc.nextInSeq(types, ds.value)
      else
        ds := Misc.prevInSeq(types, ds.value)
    }
    player.inventory.getItemStack
  }
}

class SlotControlMode(inv: IInventory, index: Int, x: Int, y: Int, ds: DataSlotBoolean) extends Slot(inv, index, x, y) with SlotClickable {
  override def onClick(clickType: ClickType, button: Int, player: EntityPlayer) = {
    if (clickType == ClickType.PICKUP && button == 0 && player.inventory.getItemStack.isEmpty) {
      ds := !ds
    }
    player.inventory.getItemStack
  }
}