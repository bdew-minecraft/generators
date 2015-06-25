/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.items

import net.bdew.lib.block.BlockRef
import net.bdew.lib.items.ItemUtils
import net.bdew.lib.multiblock.block.BlockModule
import net.bdew.lib.multiblock.tile.{TileController, TileModule}
import net.bdew.lib.nbt.NBT
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World

trait UpgradeKit extends Item {
  def canUpgradeBlock(pos: BlockRef, world: World): Boolean
  def getNewBlock(pos: BlockRef, world: World): BlockModule[_]
  def getReturnedItems(pos: BlockRef, world: World): Traversable[ItemStack]
  def saveData(te: TileModule) = NBT.from(te.writeToNBT)
  def restoreData(te: TileModule, data: NBTTagCompound) = te.readFromNBT(data)

  private def getController(pos: BlockRef, world: World) = pos.getTile[TileModule](world) flatMap (_.getCore) orElse pos.getTile[TileController](world)

  private def doUpgrade(pos: BlockRef, world: World, player: EntityPlayer): Unit = {
    if (!world.isRemote) {
      // If player not in creative, use up kit and give returned item(s)
      if (!player.capabilities.isCreativeMode) {
        player.inventory.decrStackSize(player.inventory.currentItem, 1)
        for (item <- getReturnedItems(pos, world))
          ItemUtils.dropItemToPlayer(world, player, item)
      }
      // Save old connection info
      val oldTile = pos.getTile[TileModule](world).get
      val oldConnected = oldTile.connected.value

      // Cleanly remove old tile from multiblock (if any)
      oldTile.onBreak()
      oldTile.connected.unset()

      // Let it serialize it's data
      val oldData = saveData(oldTile)

      // Place new block and grab new TE
      world.setBlock(pos.x, pos.y, pos.z, getNewBlock(pos, world), 0, 3)
      val newTile = pos.getTile[TileModule](world).get

      // Restore data and re-add to multiblock
      restoreData(newTile, oldData)
      oldConnected flatMap (_.getTile[TileController](world)) foreach newTile.connect
      getController(pos, world) foreach (_.modulesChanged = true)
    }
  }

  override def onItemUse(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, xOff: Float, yOff: Float, zOff: Float): Boolean = {
    val pos = new BlockRef(x, y, z)
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
