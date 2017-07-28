/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.efficiency

import net.bdew.generators.controllers.turbine.{MachineTurbine, TileTurbineController}
import net.bdew.generators.modules.BaseModule
import net.bdew.lib.Misc
import net.bdew.lib.PimpVanilla._
import net.bdew.lib.block.BlockTooltip
import net.bdew.lib.multiblock.tile.TileModule
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World

object BlockEfficiencyUpgradeTier2 extends BaseModule("efficiency_upgrade_tier2", "EfficiencyUpgradeTier2", classOf[TileEfficiencyUpgradeTier2]) with BlockTooltip {
  override def getTooltip(stack: ItemStack, world: World, flags: ITooltipFlag): List[String] =
    List(
      Misc.toLocalF("advgenerators.tooltip.efficiency", "%s%.0f%%%s".format(TextFormatting.YELLOW, MachineTurbine.fuelEfficiency.getDouble("Tier2") * 100, TextFormatting.RESET)),
      Misc.toLocal("advgenerators.tooltip.efficiency.req")
    ) ++ super.getTooltip(stack, world, flags)
}

class TileEfficiencyUpgradeTier2 extends TileModule {
  val kind: String = "EfficiencyUpgradeTier2"

  override def canConnectToCore(br: BlockPos): Boolean =
    super.canConnectToCore(br) && world.getTileSafe[TileTurbineController](br).exists(controller =>
      controller.getModulePositions(BlockEfficiencyUpgradeTier1).nonEmpty
    )
}

