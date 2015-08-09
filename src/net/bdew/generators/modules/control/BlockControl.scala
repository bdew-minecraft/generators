/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.control

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.bdew.generators.Generators
import net.bdew.generators.config.Config
import net.bdew.generators.modules.BaseModule
import net.bdew.lib.Misc
import net.bdew.lib.gui.GuiProvider
import net.minecraft.block.Block
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.{ChatComponentTranslation, IIcon}
import net.minecraft.world.{IBlockAccess, World}

object BlockControl extends BaseModule("Control", "Control", classOf[TileControl]) with GuiProvider {
  override def guiId = 6
  override type TEClass = TileControl

  Config.guiHandler.register(this)

  @SideOnly(Side.CLIENT)
  override def getGui(te: TEClass, player: EntityPlayer) = new GuiControl(te, player)
  override def getContainer(te: TEClass, player: EntityPlayer) = new ContainerControl(te, player)

  override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, meta: Int, xOffs: Float, yOffs: Float, zOffs: Float): Boolean = {
    if (player.isSneaking) return false
    if (world.isRemote) return true
    val te = getTE(world, x, y, z)
    if (te.getCore.isDefined) {
      player.openGui(Generators, guiId, world, x, y, z)
    } else {
      player.addChatMessage(new ChatComponentTranslation("bdlib.multiblock.notconnected"))
    }
    true
  }

  override def canConnectRedstone(world: IBlockAccess, x: Int, y: Int, z: Int, side: Int): Boolean = true

  override def getIcon(side: Int, meta: Int) =
    if (meta == 1) onIcon else blockIcon

  override def onNeighborBlockChange(world: World, x: Int, y: Int, z: Int, block: Block) {
    super.onNeighborBlockChange(world, x, y, z, block)

    val meta = world.getBlockMetadata(x, y, z)
    val powered = world.isBlockIndirectlyGettingPowered(x, y, z)
    if (powered && (meta == 0))
      world.setBlockMetadataWithNotify(x, y, z, 1, 2)
    else if (!powered && (meta > 0))
      world.setBlockMetadataWithNotify(x, y, z, 0, 2)

    if (!world.isRemote) getTE(world, x, y, z).notifyChange()
  }

  override def onBlockPlacedBy(world: World, x: Int, y: Int, z: Int, ent: EntityLivingBase, stack: ItemStack): Unit = {
    super.onBlockPlacedBy(world, x, y, z, ent, stack)
    onNeighborBlockChange(world, x, y, z, this)
  }

  var onIcon: IIcon = null

  @SideOnly(Side.CLIENT) override
  def registerBlockIcons(reg: IIconRegister): Unit = {
    blockIcon = reg.registerIcon(Misc.iconName(Generators.modId, name, "off"))
    onIcon = reg.registerIcon(Misc.iconName(Generators.modId, name, "on"))
  }
}
