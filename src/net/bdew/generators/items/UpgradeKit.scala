/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.items

import net.bdew.lib.PimpVanilla._
import net.bdew.lib.items.ItemUtils
import net.bdew.lib.multiblock.block.BlockModule
import net.bdew.lib.multiblock.tile.{TileController, TileModule}
import net.bdew.lib.nbt.NBT
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.{BlockPos, EnumFacing}
import net.minecraft.world.World

trait UpgradeKit extends Item {
  def canUpgradeBlock(pos: BlockPos, world: World): Boolean
  def getNewBlock(pos: BlockPos, world: World): BlockModule[_]
  def getReturnedItems(pos: BlockPos, world: World): Traversable[ItemStack]
  def saveData(te: TileModule) = NBT.from(te.writeToNBT)
  def restoreData(te: TileModule, data: NBTTagCompound) = te.readFromNBT(data)

  private def getController(pos: BlockPos, world: World) =
    world.getTileSafe[TileModule](pos) flatMap (_.getCore) orElse world.getTileSafe[TileController](pos)

  private def doUpgrade(pos: BlockPos, world: World, player: EntityPlayer): Unit = {
    if (!world.isRemote) {
      // If player not in creative, use up kit and give returned item(s)
      if (!player.capabilities.isCreativeMode) {
        player.inventory.decrStackSize(player.inventory.currentItem, 1)
        for (item <- getReturnedItems(pos, world))
          ItemUtils.dropItemToPlayer(world, player, item)
      }

      // Save old connection info
      val oldTile = world.getTileSafe[TileModule](pos).get
      val oldConnected = oldTile.connected.value

      // Cleanly remove old tile from multiblock (if any)
      oldTile.onBreak()
      oldTile.connected.unset()

      // Let it serialize it's data
      val oldData = saveData(oldTile)

      // Place new block and grab new TE
      world.setBlockState(pos, getNewBlock(pos, world).getDefaultState, 3)
      val newTile = world.getTileSafe[TileModule](pos).get

      // Restore data and re-add to multiblock
      restoreData(newTile, oldData)
      oldConnected flatMap (pos => world.getTileSafe[TileController](pos)) foreach newTile.connect
      getController(pos, world) foreach (_.modulesChanged = true)
    }
  }

  override def onItemUse(stack: ItemStack, player: EntityPlayer, world: World, pos: BlockPos, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean = {
    if (canUpgradeBlock(pos, world)) {
      // Clicked on block - upgrade in place
      doUpgrade(pos, world, player)
      return true
    } else {
      // Otherwise find something in multiblock to upgrade
      for {
        controller <- getController(pos, world)
        target <- controller.modules find (canUpgradeBlock(_, world))
      } {
        doUpgrade(target, world, player)
        return true
      }
    }
    false
  }

}
