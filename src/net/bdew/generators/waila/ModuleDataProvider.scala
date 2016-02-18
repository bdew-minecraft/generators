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
import net.bdew.lib.PimpVanilla._
import net.bdew.lib.data.base.UpdateKind
import net.bdew.lib.multiblock.tile.{TileController, TileModule}
import net.bdew.lib.nbt.NBT
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.BlockPos
import net.minecraft.world.World

object ModuleDataProvider extends BaseDataProvider(classOf[TileModule]) {
  override def getNBTTag(player: EntityPlayerMP, te: TileModule, tag: NBTTagCompound, world: World, pos: BlockPos): NBTTagCompound = {
    for {
      core <- te.getCore
      handler <- WailaHandler.handlers.find(_.isValidTE(core))
    } {
      tag.setTag("generators_waila_module", NBT(
        "core" -> core.getPos,
        "data" -> NBT.from(core.doSave(UpdateKind.GUI, _))
      ))
    }
    tag
  }

  override def getBodyStrings(target: TileModule, stack: ItemStack, acc: IWailaDataAccessor, cfg: IWailaConfigHandler): Iterable[String] = {
    val coreOpt = if (acc.getNBTData.hasKey("generators_waila_module")) {
      val data = acc.getNBTData.getCompoundTag("generators_waila_module")
      data.get[BlockPos]("core").flatMap { bp =>
        acc.getWorld.getTileSafe[TileController](bp).map { controller =>
          controller.doLoad(UpdateKind.GUI, data.getCompoundTag("data"))
          controller
        }
      }
    } else target.getCore
    coreOpt flatMap { core =>
      WailaHandler.handlers.find(_.isValidTE(core)) map { handler =>
        handler.getBodyStringsFromTE(core)
      }
    } getOrElse List.empty
  }
}
