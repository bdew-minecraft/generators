/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.euOutput

import com.mojang.realmsclient.gui.ChatFormatting
import net.bdew.generators.modules.BaseModule
import net.bdew.lib.multiblock.block.BlockOutput
import net.bdew.lib.rotate.BlockFacingMeta
import net.bdew.lib.{DecFormat, Misc}
import net.minecraft.block.state.IBlockState
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.{IBlockAccess, World}

class BlockEuOutputBase[T <: TileEuOutputBase](kind: String, tier: Int, maxOutput: Int, TEClass: Class[T]) extends BaseModule("eu_output_" + kind.toLowerCase, "PowerOutput", TEClass) with BlockOutput[T] with BlockFacingMeta {

  override def getDefaultFacing = EnumFacing.SOUTH

  override def setFacing(world: World, pos: BlockPos, facing: EnumFacing): Unit = {
    super.setFacing(world, pos, facing)
    getTE(world, pos).tryConnect()
  }

  override def getTooltip(stack: ItemStack, world: World, flags: ITooltipFlag): List[String] =
    Misc.toLocalF("advgenerators.tooltip.eu",
      ChatFormatting.YELLOW + tier.toString + ChatFormatting.GRAY,
      ChatFormatting.YELLOW + DecFormat.short(maxOutput) + ChatFormatting.GRAY
    ) +: super.getTooltip(stack, world, flags)

  override def canConnectRedstone(state: IBlockState, world: IBlockAccess, pos: BlockPos, side: EnumFacing): Boolean = true
}

object BlockEuOutputLV extends BlockEuOutputBase("LV", 1, 32, classOf[TileEuOutputLV])

object BlockEuOutputMV extends BlockEuOutputBase("MV", 2, 128, classOf[TileEuOutputMV])

object BlockEuOutputHV extends BlockEuOutputBase("HV", 3, 512, classOf[TileEuOutputHV])

object BlockEuOutputEV extends BlockEuOutputBase("EV", 4, 2048, classOf[TileEuOutputEV])

object BlockEuOutputIV extends BlockEuOutputBase("IV", 5, 8192, classOf[TileEuOutputIV])