/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.powerCapacitor

import java.util.Locale

import net.bdew.generators.config.{CapacitorMaterial, Config}
import net.bdew.generators.modules.BaseModule
import net.bdew.lib.block.BlockTooltip
import net.bdew.lib.{DecFormat, Misc}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.text.TextFormatting

class BlockPowerCapacitor(val material: CapacitorMaterial) extends BaseModule("power_capacitor_" + material.name.toLowerCase(Locale.US), "PowerCapacitor", classOf[TilePowerCapacitor]) with BlockTooltip {
  override def getTooltip(stack: ItemStack, player: EntityPlayer, advanced: Boolean): List[String] =
    List(Misc.toLocalF("advgenerators.tooltip.capacitor", "%s%s %s".format(
      TextFormatting.YELLOW, DecFormat.short(material.mjCapacity * Config.powerShowMultiplier), Config.powerShowUnits
    ))) ++ super.getTooltip(stack, player, advanced)
}