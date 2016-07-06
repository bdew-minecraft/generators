/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.blocks

import java.util
import java.util.Random

import net.bdew.generators.{Generators, config}
import net.bdew.lib.PimpVanilla._
import net.bdew.lib.block.BaseBlock
import net.minecraft.block.Block
import net.minecraft.block.material.{MapColor, MaterialLiquid}
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.init.Blocks
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.math.BlockPos
import net.minecraft.world.{Explosion, World}
import net.minecraftforge.fluids.{BlockFluidClassic, Fluid}

object MaterialSyngas extends MaterialLiquid(MapColor.GREEN)

object BlockSyngasFlaming extends BaseBlock("syngas_flaming", MaterialSyngas) {
  // This is a technical block used to delay chain explosions

  override def getSubBlocks(itemIn: Item, tab: CreativeTabs, list: util.List[ItemStack]): Unit = {}

  override def canDropFromExplosion(exp: Explosion) = false

  override def getBlockLayer = BlockRenderLayer.TRANSLUCENT

  override def updateTick(w: World, pos: BlockPos, state: IBlockState, rnd: Random): Unit = {
    if (!w.isRemote) {
      w.setBlockToAir(pos)
      w.createExplosion(null, pos.getX, pos.getY, pos.getZ, 5, true)
    }
  }
}

class BlockSyngas(fluid: Fluid) extends BlockFluidClassic(fluid, MaterialSyngas) {
  val openFlames = Set(Blocks.FIRE, Blocks.TORCH, Blocks.LAVA, Blocks.FLOWING_LAVA)

  setRegistryName(Generators.modId, "syngas")
  setUnlocalizedName(Generators.modId + ".syngas")

  config.Blocks.regBlock(BlockSyngasFlaming)

  override def neighborChanged(state: IBlockState, world: World, pos: BlockPos, neighborBlock: Block): Unit = {
    super.neighborChanged(state, world, pos, neighborBlock)
    if (!world.isRemote) {
      for (nPos <- pos.neighbours.values if openFlames.contains(world.getBlockState(nPos).getBlock)) {
        world.setBlockToAir(pos)
        world.createExplosion(null, pos.getX, pos.getY, pos.getZ, 5, true)
      }
    }
  }

  override def onEntityCollidedWithBlock(world: World, pos: BlockPos, state: IBlockState, ent: Entity): Unit = {
    if (ent.isBurning && !world.isRemote) {
      world.setBlockToAir(pos)
      world.createExplosion(null, pos.getX, pos.getY, pos.getZ, 5, true)
    }
  }

  override def onBlockDestroyedByExplosion(world: World, pos: BlockPos, exp: Explosion): Unit = {
    if (!world.isRemote) {
      world.scheduleUpdate(pos, BlockSyngasFlaming, world.rand.nextInt(5))
      world.setBlockState(pos, BlockSyngasFlaming.getDefaultState, 3)
    }
  }
}
