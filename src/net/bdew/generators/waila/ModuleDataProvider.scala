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
import net.bdew.lib.block.BlockRef
import net.bdew.lib.data.base.UpdateKind
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
      data.setTag("coreData", Misc.applyMutator(new NBTTagCompound) {
        core.doSave(UpdateKind.GUI, _)
      })
      tag.setTag("generators_waila_module", data)
    }
    tag
  }

  override def getBodyStrings(target: TileModule, stack: ItemStack, acc: IWailaDataAccessor, cfg: IWailaConfigHandler): Iterable[String] = {
    val coreOpt = if (acc.getNBTData.hasKey("generators_waila_module")) {
      val data = acc.getNBTData.getCompoundTag("generators_waila_module")
      val bp = BlockRef(data.getInteger("coreX"), data.getInteger("coreY"), data.getInteger("coreZ"))
      bp.getTile[TileController](acc.getWorld) map { controller =>
        controller.doLoad(UpdateKind.GUI, data.getCompoundTag("coreData"))
        controller
      }
    } else {
      target.getCore
    }
    (for {
      core <- coreOpt
      handler <- WailaHandler.handlers.find(_.isValidTE(core))
    } yield {
      handler.getBodyStringsFromTE(core)
    }).getOrElse(List.empty)
  }
}
