/*
 * Copyright (c) bdew, 2014 - 2017
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
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.ItemStack
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World

object BlockEfficiencyUpgradeTier1 extends BaseModule("efficiency_upgrade_tier1", "EfficiencyUpgradeTier1", classOf[TileEfficiencyUpgradeTier1]) with BlockTooltip {
  override def getTooltip(stack: ItemStack, world: World, flags: ITooltipFlag): List[String] =
    List(
      Misc.toLocalF("advgenerators.tooltip.efficiency", "%s%.0f%%%s".format(TextFormatting.YELLOW, MachineTurbine.fuelEfficiency.getDouble("Tier1") * 100, TextFormatting.RESET))
    ) ++ super.getTooltip(stack, world, flags)
}

class TileEfficiencyUpgradeTier1 extends TileModule {
  val kind: String = "EfficiencyUpgradeTier1"

  override def connect(target: TileController): Unit = {
    super.connect(target)
    (target.modules.set ++ Set(target.getPos)).flatMap(_.neighbours.values)
      .flatMap(pos => world.getTileSafe[TileEfficiencyUpgradeTier2](pos))
      .filter(_.connected.isEmpty)
      .foreach(_.tryConnect())
  }
}

