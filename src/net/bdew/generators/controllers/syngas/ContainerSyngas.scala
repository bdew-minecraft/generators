/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.controllers.syngas

import net.bdew.generators.network.ContainerCanDumpBuffers
import net.bdew.lib.data.base.ContainerDataSlots
import net.bdew.lib.gui.{BaseContainer, SlotValidating}
import net.bdew.lib.multiblock.interact.ContainerOutputFaces
import net.minecraft.entity.player.EntityPlayer

class ContainerSyngas(val te: TileSyngasController, player: EntityPlayer) extends BaseContainer(te.inventory) with ContainerDataSlots with ContainerOutputFaces with ContainerCanDumpBuffers {
  lazy val dataSource = te

  addSlotToContainer(new SlotValidating(te.inventory, 0, 77, 61))
  addSlotToContainer(new SlotValidating(te.inventory, 1, 95, 61))
  addSlotToContainer(new SlotValidating(te.inventory, 2, 113, 61))
  addSlotToContainer(new SlotValidating(te.inventory, 3, 131, 61))

  bindPlayerInventory(player.inventory, 8, 94, 152)

  override def dumpBuffers(): Unit = {
    te.waterTank.setFluid(null)
    te.syngasTank.setFluid(null)
    te.steamBuffer := 0
    te.carbonBuffer := 0
  }
}
