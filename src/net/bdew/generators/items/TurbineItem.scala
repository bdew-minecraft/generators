/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.items

import java.util

import net.bdew.generators.config.TurbineMaterial
import net.bdew.generators.modules.turbine.BlockTurbine
import net.bdew.lib.Misc
import net.bdew.lib.PimpVanilla._
import net.bdew.lib.items.BaseItem
import net.bdew.lib.multiblock.block.BlockModule
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class TurbineItem(val material: TurbineMaterial, val kind: String) extends BaseItem("Turbine" + kind + material.name)

class TurbineUpgradeKit(material: TurbineMaterial) extends TurbineItem(material, "Kit") with UpgradeKit {
  override def canUpgradeBlock(pos: BlockPos, world: World): Boolean =
    world.getBlockSafe[BlockTurbine](pos).exists(_.material.tier < material.tier)

  override def getNewBlock(pos: BlockPos, world: World): BlockModule[_] =
    material.turbineBlock.get

  override def getReturnedItems(pos: BlockPos, world: World): List[ItemStack] =
    List(new ItemStack((world.getBlockSafe[BlockTurbine](pos) flatMap { block => block.material.rotorItem }).get))

  override def addInformation(stack: ItemStack, player: EntityPlayer, tooltip: util.List[String], advanced: Boolean): Unit = {
    tooltip.add(Misc.toLocal("advgenerators.tooltip.kit"))
    super.addInformation(stack, player, tooltip, advanced)
  }
}