package net.bdew.generators.control

import net.bdew.lib.Misc
import net.bdew.lib.container.SlotClickable
import net.bdew.lib.data.DataSlotBoolean
import net.minecraft.world.Container
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.{ClickType, Slot}
import net.minecraft.world.item.ItemStack

class SlotControlAction(inv: Container, index: Int, x: Int, y: Int, ds: DataSlotControlAction, types: => Seq[ControlAction]) extends Slot(inv, index, x, y) with SlotClickable {
  override def onClick(clickType: ClickType, button: Int, player: Player): ItemStack = {
    if (clickType == ClickType.PICKUP && (button == 0 || button == 1) && types.nonEmpty && player.containerMenu.getCarried.isEmpty) {
      if (button == 0)
        ds := Misc.nextInSeq(types, ds.value)
      else
        ds := Misc.prevInSeq(types, ds.value)
    }
    player.containerMenu.getCarried
  }
}

class SlotControlMode(inv: Container, index: Int, x: Int, y: Int, ds: DataSlotBoolean) extends Slot(inv, index, x, y) with SlotClickable {
  override def onClick(clickType: ClickType, button: Int, player: Player): ItemStack = {
    if (clickType == ClickType.PICKUP && button == 0 && player.containerMenu.getCarried.isEmpty) {
      ds := !ds
    }
    player.containerMenu.getCarried
  }
}