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
import net.bdew.lib.block.BlockRef
import net.bdew.lib.multiblock.tile.{TileController, TileModule}
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World

object ModuleDataProvider extends BaseDataProvider(classOf[TileModule]) {
  override def getNBTTag(player: EntityPlayerMP, te: TileModule, tag: NBTTagCompound, world: World, x: Int, y: Int, z: Int): NBTTagCompound = {
    val data = new NBTTagCompound
    for {
      core <- te.getCore
      handler <- WailaHandler.handlers.find(_.isValidTE(core))
    } {
      data.setInteger("coreX", core.xCoord)
      data.setInteger("coreY", core.yCoord)
      data.setInteger("coreZ", core.zCoord)
      data.setTag("coreData", handler.getNBTTag(player, core, new NBTTagCompound, world, core.xCoord, core.yCoord, core.zCoord))
      tag.setTag("generators_waila_core", data)
    }
    tag
  }

  override def getBodyStrings(target: TileModule, stack: ItemStack, acc: IWailaDataAccessor, cfg: IWailaConfigHandler): Iterable[String] = {
    if (acc.getNBTData.hasKey("generators_waila_core")) {
      val coreData = acc.getNBTData.getCompoundTag("generators_waila_core")
      val bp = BlockRef(coreData.getInteger("coreX"), coreData.getInteger("coreY"), coreData.getInteger("coreZ"))
      (for {
        core <- bp.getTile[TileController](acc.getWorld)
        handler <- WailaHandler.handlers.find(_.isValidTE(core))
      } yield {
        handler.getBodyStringsFromData(core, coreData.getCompoundTag("coreData"))
      }).getOrElse(List.empty)
    } else List.empty
  }
}
