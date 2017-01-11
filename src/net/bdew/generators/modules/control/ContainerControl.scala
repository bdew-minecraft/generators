/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.control

import net.bdew.generators.control.{ControlActions, SlotControlAction, SlotControlMode}
import net.bdew.lib.data.base.ContainerDataSlots
import net.bdew.lib.gui.NoInvContainer
import net.bdew.lib.tile.inventory.SimpleInventory
import net.minecraft.entity.player.EntityPlayer

class ContainerControl(val te: TileControl, player: EntityPlayer) extends NoInvContainer with ContainerDataSlots {
  lazy val dataSource = te

  val fakeInv = new SimpleInventory(2)

  addSlotToContainer(new SlotControlAction(fakeInv, 0, 98, 38, te.action,
    te.getCore map (_.availableControlActions) getOrElse List(ControlActions.disabled)))
  addSlotToContainer(new SlotControlMode(fakeInv, 1, 62, 38, te.mode))

  bindPlayerInventory(player.inventory, 8, 94, 152)

  override def canInteractWith(player: EntityPlayer) = true
}
