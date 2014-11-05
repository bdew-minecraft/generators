/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.bdew.generators.GeneratorsResourceProvider
import net.bdew.lib.Misc
import net.bdew.lib.block.NamedBlock
import net.bdew.lib.multiblock.block.BlockModule
import net.bdew.lib.multiblock.tile.TileModule
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister

class BaseModule[T <: TileModule](name: String, kind: String, TEClass: Class[T])
  extends BlockModule(name, kind, Material.iron, TEClass) with NamedBlock {
  val mod = Misc.getActiveModId
  override def resources = GeneratorsResourceProvider

  setBlockName(mod + "." + name)
  setHardness(1)

  @SideOnly(Side.CLIENT)
  override def registerBlockIcons(ir: IIconRegister) {
    blockIcon = ir.registerIcon(mod + ":" + name.toLowerCase + "/main")
    regIcons(ir)
  }

  @SideOnly(Side.CLIENT)
  def regIcons(ir: IIconRegister) {}
}
