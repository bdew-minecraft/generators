/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.blocks

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.bdew.generators.Generators
import net.minecraft.block.material.{MapColor, MaterialLiquid}
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.util.IIcon
import net.minecraftforge.fluids.{BlockFluidClassic, Fluid}

object MaterialSteam extends MaterialLiquid(MapColor.snowColor)

class BlockSteam(fluid: Fluid) extends BlockFluidClassic(fluid, MaterialSteam) {
  val ownIcons = fluid.getIcon == null

  setBlockName(Generators.modId + ".steam")

  @SideOnly(Side.CLIENT)
  override def getIcon(side: Int, meta: Int): IIcon =
    if (side == 0 || side == 1)
      fluid.getStillIcon
    else
      fluid.getFlowingIcon

  @SideOnly(Side.CLIENT)
  override def registerBlockIcons(register: IIconRegister) {
    if (ownIcons) {
      fluid.setStillIcon(register.registerIcon(Generators.modId + "steam/still"))
      fluid.setFlowingIcon(register.registerIcon(Generators.modId + "steam/flowing"))
    }
  }
}
