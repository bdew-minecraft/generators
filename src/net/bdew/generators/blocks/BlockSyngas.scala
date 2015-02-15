/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.blocks

import java.util
import java.util.Random

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.bdew.generators.{Generators, config}
import net.bdew.lib.block.{BlockRef, SimpleBlock}
import net.minecraft.block.Block
import net.minecraft.block.material.{MapColor, MaterialLiquid}
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraft.util.IIcon
import net.minecraft.world.{Explosion, World}
import net.minecraftforge.fluids.{BlockFluidClassic, Fluid}

object MaterialSyngas extends MaterialLiquid(MapColor.greenColor)

object BlockSyngasFlaming extends SimpleBlock("syngas_flaming", MaterialSyngas) {
  // This is a technical block used to delay chain explosions

  override def getSubBlocks(item: Item, tab: CreativeTabs, list: util.List[_]) = {}

  override def getRenderBlockPass = 1

  override def canDropFromExplosion(exp: Explosion) = false

  override def updateTick(w: World, x: Int, y: Int, z: Int, rnd: Random): Unit = {
    if (!w.isRemote) {
      w.setBlockToAir(x, y, z)
      w.createExplosion(null, x, y, z, 5, true)
    }
  }

  override def getIcon(side: Int, meta: Int): IIcon =
    config.Blocks.syngasFluid.getStillIcon
}

class BlockSyngas(fluid: Fluid) extends BlockFluidClassic(fluid, MaterialSyngas) {
  val ownIcons = fluid.getIcon == null

  setBlockName(Generators.modId + ".syngas")

  config.Blocks.regBlock(BlockSyngasFlaming)

  override def onNeighborBlockChange(world: World, x: Int, y: Int, z: Int, block: Block): Unit = {
    super.onNeighborBlockChange(world, x, y, z, block)
    if (!world.isRemote) {
      for {
        (dir, nRef) <- BlockRef(x, y, z).neighbours
        nBlock <- nRef.block(world) if nBlock == Blocks.fire || nBlock == Blocks.torch
      } {
        world.setBlockToAir(x, y, z)
        world.createExplosion(null, x, y, z, 5, true)
      }
    }
  }

  override def onBlockDestroyedByExplosion(world: World, x: Int, y: Int, z: Int, exp: Explosion): Unit = {
    if (!world.isRemote) {
      world.scheduleBlockUpdate(x, y, z, BlockSyngasFlaming, world.rand.nextInt(5))
      world.setBlock(x, y, z, BlockSyngasFlaming, 15, 3)
    }
  }

  @SideOnly(Side.CLIENT)
  override def getIcon(side: Int, meta: Int): IIcon =
    if (side == 0 || side == 1)
      fluid.getStillIcon
    else
      fluid.getFlowingIcon

  @SideOnly(Side.CLIENT)
  override def registerBlockIcons(register: IIconRegister) {
    if (ownIcons) {
      fluid.setStillIcon(register.registerIcon(Generators.modId + ":syngas/still"))
      fluid.setFlowingIcon(register.registerIcon(Generators.modId + ":syngas/flowing"))
    }
  }
}
