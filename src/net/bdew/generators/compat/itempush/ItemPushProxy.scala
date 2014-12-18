/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.compat.itempush

import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection

trait ItemPushProxy {
  def pushStack(from: TileEntity, dir: ForgeDirection, stack: ItemStack): ItemStack
  def isValidTarget(from: TileEntity, dir: ForgeDirection): Boolean
}
