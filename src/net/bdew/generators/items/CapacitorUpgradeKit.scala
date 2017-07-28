/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.items

import java.util
import java.util.Locale

import net.bdew.generators.config.CapacitorMaterial
import net.bdew.generators.modules.powerCapacitor.BlockPowerCapacitor
import net.bdew.lib.Misc
import net.bdew.lib.PimpVanilla._
import net.bdew.lib.items.BaseItem
import net.bdew.lib.multiblock.block.BlockModule
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class CapacitorUpgradeKit(val material: CapacitorMaterial) extends BaseItem("capacitor_kit_" + material.name.toLowerCase(Locale.US)) with UpgradeKit {
  override def getNewBlock(pos: BlockPos, world: World): BlockModule[_] = material.capacitorBlock.get

  override def getReturnedItems(pos: BlockPos, world: World): List[ItemStack] = List.empty

  override def canUpgradeBlock(pos: BlockPos, world: World): Boolean =
    world.getBlockSafe[BlockPowerCapacitor](pos).exists(_.material.tier < material.tier)

  override def addInformation(stack: ItemStack, world: World, tooltip: util.List[String], flagIn: ITooltipFlag): Unit = {
    tooltip.add(Misc.toLocal("advgenerators.tooltip.kit"))
    super.addInformation(stack, world, tooltip, flagIn)
  }
}
