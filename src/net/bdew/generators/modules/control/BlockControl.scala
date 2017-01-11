/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.control

import net.bdew.generators.Generators
import net.bdew.generators.config.Config
import net.bdew.generators.modules.BaseModule
import net.bdew.lib.gui.GuiProvider
import net.minecraft.block.Block
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.{EnumFacing, EnumHand}
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

object BlockControl extends BaseModule("Control", "Control", classOf[TileControl]) with GuiProvider {
  override def guiId = 6
  override type TEClass = TileControl

  object Properties {
    val POWERED = PropertyBool.create("powered")
  }

  override def getProperties = super.getProperties :+ Properties.POWERED

  setDefaultState(getDefaultState.withProperty(Properties.POWERED, Boolean.box(true)))
  Config.guiHandler.register(this)

  @SideOnly(Side.CLIENT)
  override def getGui(te: TEClass, player: EntityPlayer) = new GuiControl(te, player)
  override def getContainer(te: TEClass, player: EntityPlayer) = new ContainerControl(te, player)

  override def onBlockActivated(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer, hand: EnumHand, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean = {
    if (player.isSneaking) return false
    if (world.isRemote) return true
    val te = getTE(world, pos)
    if (te.getCore.isDefined) {
      player.openGui(Generators, guiId, world, pos.getX, pos.getY, pos.getZ)
    } else {
      player.sendMessage(new TextComponentTranslation("bdlib.multiblock.notconnected"))
    }
    true
  }

  override def canConnectRedstone(state: IBlockState, world: IBlockAccess, pos: BlockPos, side: EnumFacing): Boolean = true

  override def getMetaFromState(state: IBlockState): Int =
    if (state.getValue(Properties.POWERED)) 1 else 0

  override def getStateFromMeta(meta: Int): IBlockState =
    getDefaultState.withProperty(Properties.POWERED, Boolean.box(meta > 0))

  override def neighborChanged(state: IBlockState, world: World, pos: BlockPos, block: Block, fromPos: BlockPos): Unit = {
    super.neighborChanged(state, world, pos, block, fromPos)
    val wasPowered = state.getValue(Properties.POWERED)
    val powered = world.isBlockIndirectlyGettingPowered(pos)
    if ((powered > 0) ^ wasPowered)
      world.setBlockState(pos, state.withProperty(Properties.POWERED, Boolean.box(powered > 0)))
    if (!world.isRemote) getTE(world, pos).notifyChange()
  }

  override def onBlockPlacedBy(world: World, pos: BlockPos, state: IBlockState, placer: EntityLivingBase, stack: ItemStack): Unit = {
    super.onBlockPlacedBy(world, pos, state, placer, stack)
    neighborChanged(state, world, pos, this, pos)
  }
}
