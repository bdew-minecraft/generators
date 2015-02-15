/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license
 */

package net.bdew.generators.waila

import mcp.mobius.waila.api.{IWailaConfigHandler, IWailaDataAccessor}
import net.bdew.lib.Misc
import net.bdew.lib.data.base.UpdateKind
import net.bdew.lib.multiblock.tile.TileController
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World

abstract class BaseControllerDataProvider[T <: TileController](cls: Class[T]) extends BaseDataProvider(cls) {

  override def getNBTTag(player: EntityPlayerMP, te: T, tag: NBTTagCompound, world: World, x: Int, y: Int, z: Int): NBTTagCompound = {
    tag.setTag("generators_waila_data", Misc.applyMutator(new NBTTagCompound) {
      te.doSave(UpdateKind.GUI, _)
    })
    tag
  }

  override def getBodyStrings(target: T, stack: ItemStack, acc: IWailaDataAccessor, cfg: IWailaConfigHandler) = getBodyStringsFromData(target, acc.getNBTData)

  def loadData(te: T, data: NBTTagCompound): Unit = {
    if (data.hasKey("generators_waila_data"))
      te.doLoad(UpdateKind.GUI, data.getCompoundTag("generators_waila_data"))
  }

  def getBodyStringsFromData(te: T, data: NBTTagCompound): Iterable[String]
}
