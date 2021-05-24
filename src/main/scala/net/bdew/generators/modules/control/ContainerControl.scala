package net.bdew.generators.modules.control

import net.bdew.generators.control.{ControlActions, SlotControlAction, SlotControlMode}
import net.bdew.generators.registries.Containers
import net.bdew.lib.container.NoInvContainer
import net.bdew.lib.data.base.ContainerDataSlots
import net.bdew.lib.inventory.SimpleInventory
import net.minecraft.entity.player.PlayerInventory

class ContainerControl(val te: TileControl, playerInventory: PlayerInventory, id: Int)
  extends NoInvContainer(Containers.control.get(), id) with ContainerDataSlots {

  lazy val dataSource: TileControl = te

  val fakeInv = new SimpleInventory(2)

  addSlot(new SlotControlAction(fakeInv, 0, 98, 38, te.action,
    te.getCore map (_.availableControlActions) getOrElse List(ControlActions.disabled)))
  addSlot(new SlotControlMode(fakeInv, 1, 62, 38, te.mode))

  bindPlayerInventory(playerInventory, 8, 94, 152)
}
