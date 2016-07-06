/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.controllers.exchanger

import net.bdew.generators.network.ContainerCanDumpBuffers
import net.bdew.lib.data.base.ContainerDataSlots
import net.bdew.lib.gui.NoInvContainer
import net.bdew.lib.multiblock.interact.ContainerOutputFaces
import net.minecraft.entity.player.EntityPlayer

class ContainerExchanger(val te: TileExchangerController, player: EntityPlayer) extends NoInvContainer with ContainerDataSlots with ContainerOutputFaces with ContainerCanDumpBuffers {
  lazy val dataSource = te

  bindPlayerInventory(player.inventory, 8, 94, 152)

  def canInteractWith(entityPlayer: EntityPlayer) = true

  override def dumpBuffers(): Unit = {
    te.heaterIn.rawDrain(Double.MaxValue, false, true)
    te.coolerIn.rawDrain(Double.MaxValue, false, true)
    te.heaterOut.rawDrain(Double.MaxValue, false, true)
    te.coolerOut.rawDrain(Double.MaxValue, false, true)
  }
}
