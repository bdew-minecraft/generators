/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.waila

import net.bdew.lib.multiblock.tile.TileController
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

case class ControllerHandlerPair[T <: TileController](teClass: Class[T], handler: BaseControllerDataProvider[T]) {
  def isValidTE(x: TileController) = teClass.isInstance(x)
  def getBodyStringsFromTE(te: TileController) = handler.getBodyStringsFromTE(te.asInstanceOf[T])
  def getNBTTag(player: EntityPlayerMP, te: TileController, tag: NBTTagCompound, world: World, pos: BlockPos) =
    handler.getNBTTag(player, te.asInstanceOf[T], tag, world, pos)
}
