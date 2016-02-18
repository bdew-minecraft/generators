/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.efficiency

import net.bdew.generators.controllers.turbine.MachineTurbine
import net.bdew.generators.modules.BaseModule
import net.bdew.lib.Misc
import net.bdew.lib.PimpVanilla._
import net.bdew.lib.block.BlockTooltip
import net.bdew.lib.multiblock.tile.{TileController, TileModule}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumChatFormatting

object BlockEfficiencyUpgradeTier1 extends BaseModule("EfficiencyUpgradeTier1", "EfficiencyUpgradeTier1", classOf[TileEfficiencyUpgradeTier1]) with BlockTooltip {
  override def getTooltip(stack: ItemStack, player: EntityPlayer, advanced: Boolean): List[String] =
    List(
      Misc.toLocalF("advgenerators.tooltip.efficiency", "%s%.0f%%%s".format(EnumChatFormatting.YELLOW, MachineTurbine.fuelEfficiency.getDouble("Tier1") * 100, EnumChatFormatting.RESET))
    ) ++ super.getTooltip(stack, player, advanced)
}

class TileEfficiencyUpgradeTier1 extends TileModule {
  val kind: String = "EfficiencyUpgradeTier1"

  override def connect(target: TileController): Unit = {
    super.connect(target)
    (target.modules.set ++ Set(target.getPos)).flatMap(_.neighbours.values)
      .flatMap(pos => worldObj.getTileSafe[TileEfficiencyUpgradeTier2](pos))
      .filter(_.connected.isEmpty)
      .foreach(_.tryConnect())
  }
}

