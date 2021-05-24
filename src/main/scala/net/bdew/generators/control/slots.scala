package net.bdew.generators.control

import net.bdew.lib.Misc
import net.bdew.lib.container.SlotClickable
import net.bdew.lib.data.DataSlotBoolean
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.container.{ClickType, Slot}
import net.minecraft.item.ItemStack

class SlotControlAction(inv: IInventory, index: Int, x: Int, y: Int, ds: DataSlotControlAction, types: => Seq[ControlAction]) extends Slot(inv, index, x, y) with SlotClickable {
  override def onClick(clickType: ClickType, button: Int, player: PlayerEntity): ItemStack = {
    if (clickType == ClickType.PICKUP && (button == 0 || button == 1) && types.nonEmpty && player.inventory.getCarried.isEmpty) {
      if (button == 0)
        ds := Misc.nextInSeq(types, ds.value)
      else
        ds := Misc.prevInSeq(types, ds.value)
    }
    player.inventory.getCarried
  }
}

class SlotControlMode(inv: IInventory, index: Int, x: Int, y: Int, ds: DataSlotBoolean) extends Slot(inv, index, x, y) with SlotClickable {
  override def onClick(clickType: ClickType, button: Int, player: PlayerEntity): ItemStack = {
    if (clickType == ClickType.PICKUP && button == 0 && player.inventory.getCarried.isEmpty) {
      ds := !ds
    }
    player.inventory.getCarried
  }
}