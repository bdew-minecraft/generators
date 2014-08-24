/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.blocks.turbineController

import net.bdew.lib.data.base.ContainerDataSlots
import net.bdew.lib.gui.NoInvContainer
import net.bdew.lib.multiblock.interact.ContainerOutputFaces
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Slot

class ContainerTurbine(val te: TileTurbineController, player: EntityPlayer) extends NoInvContainer with ContainerDataSlots with ContainerOutputFaces {
  lazy val dataSource = te

  for (i <- 0 until 9)
    addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 136))

  def canInteractWith(entityplayer: EntityPlayer) = true
}
