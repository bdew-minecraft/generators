/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.compat.itempush

import net.bdew.lib.Misc
import net.bdew.lib.items.ItemUtils
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.{Capability, CapabilityInject}
import net.minecraftforge.items.IItemHandler

import scala.annotation.meta.setter

object VanillaPush extends ItemPushProxy {
  @(CapabilityInject@setter)(classOf[IItemHandler])
  var CAP: Capability[IItemHandler] = null

  override def pushStack(from: TileEntity, dir: EnumFacing, stack: ItemStack) =
    Misc.getNeighbourTileCapability(from, dir, CAP) map { cap =>
      ItemUtils.addStackToHandler(stack, cap)
    } getOrElse stack

  override def isValidTarget(from: TileEntity, dir: EnumFacing) =
    Misc.neighbourTileHasCapability(from, dir, CAP)
}
