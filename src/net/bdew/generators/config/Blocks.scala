/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.config

import net.bdew.generators.CreativeTabsGenerators
import net.bdew.generators.compat.PowerProxy
import net.bdew.generators.modules.euOutput.{BlockEuOutputHV, BlockEuOutputLV, BlockEuOutputMV}
import net.bdew.generators.modules.fluidInput.BlockFluidInput
import net.bdew.generators.modules.fuelTank.BlockFuelTank
import net.bdew.generators.modules.mjOutput.BlockMjOutput
import net.bdew.generators.modules.powerCapacitor.BlockPowerCapacitor
import net.bdew.generators.modules.rfOutput.BlockRfOutput
import net.bdew.generators.modules.turbine.BlockTurbine
import net.bdew.lib.config.BlockManager

object Blocks extends BlockManager(CreativeTabsGenerators.main) {
  regBlock(BlockFluidInput)

  if (PowerProxy.haveBC)
    regBlock(BlockMjOutput)

  if (PowerProxy.haveIC2) {
    regBlock(BlockEuOutputLV)
    regBlock(BlockEuOutputMV)
    regBlock(BlockEuOutputHV)
  }

  if (PowerProxy.haveTE)
    regBlock(BlockRfOutput)

  regBlock(BlockTurbine)
  regBlock(BlockFuelTank)
  regBlock(BlockPowerCapacitor)
}