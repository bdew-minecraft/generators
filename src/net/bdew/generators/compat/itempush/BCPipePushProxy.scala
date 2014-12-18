/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.compat.itempush

import buildcraft.api.transport.IPipeTile
import net.bdew.lib.Misc
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection

object BCPipePushProxy extends ItemPushProxy {
  override def pushStack(from: TileEntity, dir: ForgeDirection, stack: ItemStack) =
    (for (pipe <- Misc.getNeighbourTile(from, dir, classOf[IPipeTile])) yield {
      if (pipe.getPipeType == IPipeTile.PipeType.ITEM) {
        val used = pipe.injectItem(stack, true, dir.getOpposite)
        if (used >= stack.stackSize)
          null
        else
          stack.splitStack(stack.stackSize - used)
      } else stack
    }).getOrElse(stack)

  override def isValidTarget(from: TileEntity, dir: ForgeDirection) =
    Misc.getNeighbourTile(from, dir, classOf[IPipeTile]).exists(_.getPipeType == IPipeTile.PipeType.ITEM)
}
