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
import net.bdew.generators.config.TurbineMaterial
import net.bdew.generators.modules.turbine.BlockTurbine
import net.bdew.lib.Misc
import net.bdew.lib.block.BlockRef
import net.bdew.lib.items.NamedItem
import net.bdew.lib.multiblock.block.BlockModule
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class TurbineItem(val material: TurbineMaterial, val kind: String) extends NamedItem {
  override def name = "Turbine" + kind + material.name

  setUnlocalizedName("advgenerators." + name)

  @SideOnly(Side.CLIENT)
  override def registerIcons(reg: IIconRegister): Unit = {
    itemIcon = reg.registerIcon("advgenerators:turbine/%s/%s".format(material.name.toLowerCase, kind.toLowerCase))
  }
}

class TurbineUpgradeKit(material: TurbineMaterial) extends TurbineItem(material, "Kit") with UpgradeKit {
  override def canUpgradeBlock(pos: BlockRef, world: World): Boolean =
    pos.getBlock[BlockTurbine](world).exists(_.material.tier < material.tier)

  override def getNewBlock(pos: BlockRef, world: World): BlockModule[_] =
    material.turbineBlock.get

  override def getReturnedItem(pos: BlockRef, world: World): ItemStack =
    new ItemStack((pos.getBlock[BlockTurbine](world) flatMap { block => block.material.rotorItem }).get)

  override def addInformation(stack: ItemStack, player: EntityPlayer, list: util.List[_], detailed: Boolean): Unit = {
    list.asInstanceOf[java.util.List[String]].add(Misc.toLocal("advgenerators.tooltip.kit"))
    super.addInformation(stack, player, list, detailed)
  }
}