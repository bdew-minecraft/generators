/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.blocks.turbineController

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.bdew.generators.blocks.BaseController
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.util.IIcon
import net.minecraftforge.common.util.ForgeDirection

object BlockTurbineController extends BaseController("TurbineController", classOf[TileTurbineController]) {
  var topIcon: IIcon = null
  var bottomIcon: IIcon = null

  @SideOnly(Side.CLIENT)
  override def regIcons(ir: IIconRegister) {
    topIcon = ir.registerIcon("advgenerators:" + name.toLowerCase + "/top")
    bottomIcon = ir.registerIcon("advgenerators:" + name.toLowerCase + "/bottom")
  }

  override def getIcon(side: Int, meta: Int) =
    if (side == ForgeDirection.UP.ordinal())
      topIcon
    else if (side == ForgeDirection.DOWN.ordinal())
      bottomIcon
    else
      blockIcon

}
