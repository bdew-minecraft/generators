/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.turbine

import java.util.Locale

import net.bdew.generators.config.{Config, TurbineMaterial}
import net.bdew.generators.modules.BaseModule
import net.bdew.lib.block.BlockTooltip
import net.bdew.lib.{DecFormat, Misc}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.text.TextFormatting

class BlockTurbine(val material: TurbineMaterial) extends BaseModule("turbine_" + material.name.toLowerCase(Locale.US), "Turbine", classOf[TileTurbine]) with BlockTooltip {
  override def getTooltip(stack: ItemStack, player: EntityPlayer, advanced: Boolean): List[String] = {
    List(
      Misc.toLocalF("advgenerators.tooltip.turbine.produce", "%s%s %s/t".format(
        TextFormatting.YELLOW, DecFormat.short(material.maxMJPerTick * Config.powerShowMultiplier), Config.powerShowUnits
      )),
      Misc.toLocalF("advgenerators.tooltip.turbine.inertia", TextFormatting.YELLOW + "%.0f%%".format(100D * material.inertiaMultiplier))
    ) ++ super.getTooltip(stack, player, advanced)
  }
}
