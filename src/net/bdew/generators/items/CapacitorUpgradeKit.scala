/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.items

import java.util

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.bdew.generators.Generators
import net.bdew.generators.config.CapacitorMaterial
import net.bdew.generators.modules.powerCapacitor.BlockPowerCapacitor
import net.bdew.lib.Misc
import net.bdew.lib.block.BlockRef
import net.bdew.lib.items.NamedItem
import net.bdew.lib.multiblock.block.BlockModule
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class CapacitorUpgradeKit(val material: CapacitorMaterial) extends NamedItem with UpgradeKit {
  override def name: String = "CapacitorKit" + material.name
  setUnlocalizedName("advgenerators." + name)

  @SideOnly(Side.CLIENT)
  override def registerIcons(reg: IIconRegister): Unit = {
    itemIcon = reg.registerIcon(Misc.iconName(Generators.modId, "capacitorkit", material.name))
  }

  override def getNewBlock(pos: BlockRef, world: World): BlockModule[_] = material.capacitorBlock.get

  override def getReturnedItems(pos: BlockRef, world: World): List[ItemStack] = List.empty

  override def canUpgradeBlock(pos: BlockRef, world: World): Boolean =
    pos.getBlock[BlockPowerCapacitor](world).exists(_.material.tier < material.tier)

  override def addInformation(stack: ItemStack, player: EntityPlayer, list: util.List[_], detailed: Boolean): Unit = {
    list.asInstanceOf[java.util.List[String]].add(Misc.toLocal("advgenerators.tooltip.kit"))
    super.addInformation(stack, player, list, detailed)
  }
}
