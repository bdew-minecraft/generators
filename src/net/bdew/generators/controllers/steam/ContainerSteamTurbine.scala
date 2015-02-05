/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license
 */

package net.bdew.generators.controllers.steam

import net.bdew.lib.data.base.ContainerDataSlots
import net.bdew.lib.gui.NoInvContainer
import net.bdew.lib.multiblock.interact.ContainerOutputFaces
import net.minecraft.entity.player.EntityPlayer

class ContainerSteamTurbine(val te: TileSteamTurbineController, player: EntityPlayer) extends NoInvContainer with ContainerDataSlots with ContainerOutputFaces {
  lazy val dataSource = te

  bindPlayerInventory(player.inventory, 8, 94, 152)

  def canInteractWith(entityPlayer: EntityPlayer) = true
}
