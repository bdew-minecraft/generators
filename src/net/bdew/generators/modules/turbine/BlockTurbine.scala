/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.turbine

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.bdew.generators.config.{Config, TurbineMaterial}
import net.bdew.generators.modules.BaseModule
import net.bdew.lib.block.BlockTooltip
import net.bdew.lib.{DecFormat, Misc}
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.{EnumChatFormatting, IIcon}
import net.minecraftforge.common.util.ForgeDirection

class BlockTurbine(val material: TurbineMaterial) extends BaseModule("Turbine" + material.name, "Turbine", classOf[TileTurbine]) with BlockTooltip {
  var topIcon: IIcon = null

  override def getIcon(side: Int, meta: Int) =
    if (side == ForgeDirection.UP.ordinal() || side == ForgeDirection.DOWN.ordinal()) topIcon else blockIcon

  override def getTooltip(stack: ItemStack, player: EntityPlayer, advanced: Boolean): List[String] = {
    List(
      Misc.toLocalF("advgenerators.tooltip.turbine.produce", "%s%s %s/t".format(
        EnumChatFormatting.YELLOW, DecFormat.short(material.maxMJPerTick * Config.powerShowMultiplier), Config.powerShowUnits
      )),
      Misc.toLocalF("advgenerators.tooltip.turbine.inertia", EnumChatFormatting.YELLOW + "%.0f%%".format(100D * material.inertiaMultiplier))
    )
  }

  @SideOnly(Side.CLIENT)
  override def registerBlockIcons(ir: IIconRegister) {
    blockIcon = ir.registerIcon("advgenerators:turbine/" + material.name.toLowerCase + "/main")
    topIcon = ir.registerIcon("advgenerators:turbine/" + material.name.toLowerCase + "/top")
  }
}
