/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.config

import net.bdew.generators.blocks.BlockSteam
import net.bdew.generators.compat.PowerProxy
import net.bdew.generators.modules.euOutput.{BlockEuOutputHV, BlockEuOutputLV, BlockEuOutputMV}
import net.bdew.generators.modules.exchanger.BlockExchanger
import net.bdew.generators.modules.fluidInput.BlockFluidInput
import net.bdew.generators.modules.fluidOutputSelect.BlockFluidOutputSelect
import net.bdew.generators.modules.fuelTank.BlockFuelTank
import net.bdew.generators.modules.itemOutput.BlockItemOutput
import net.bdew.generators.modules.powerCapacitor.BlockPowerCapacitor
import net.bdew.generators.modules.pressure.{BlockPressureOutput, BlockPressureInput}
import net.bdew.generators.modules.rfOutput.BlockRfOutput
import net.bdew.generators.modules.turbine.BlockTurbine
import net.bdew.generators.{CreativeTabsGenerators, Generators}
import net.bdew.lib.Misc
import net.bdew.lib.config.BlockManager
import net.bdew.pressure.api.PressureAPI
import net.minecraft.item.EnumRarity
import net.minecraftforge.fluids.{Fluid, FluidRegistry}

object Blocks extends BlockManager(CreativeTabsGenerators.main) {
  regBlock(BlockFluidInput)

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

  regBlock(BlockExchanger)

  regBlock(BlockFluidOutputSelect)

  regBlock(BlockItemOutput)

  if (Misc.haveModVersion("pressure") && PressureAPI.HELPER != null) {
    Generators.logInfo("Pressure pipes detected (%s), adding pressure modules", PressureAPI.HELPER)
    regBlock(BlockPressureInput)
    regBlock(BlockPressureOutput)
  }

  val steamFluid = if (!FluidRegistry.isFluidRegistered("steam")) {
    Generators.logInfo("Steam not registered by any other mod, creating...")
    val newSteam = new Fluid("steam") // Values shamelessly stolen from BR
      .setTemperature(1000)
      .setGaseous(true)
      .setLuminosity(0)
      .setRarity(EnumRarity.common)
      .setDensity(-10)
    FluidRegistry.registerFluid(newSteam)
    newSteam
  } else FluidRegistry.getFluid("steam")

  if (steamFluid.getBlock == null) {
    Generators.logInfo("Adding steam block")
    steamFluid.setBlock(regBlock(new BlockSteam(steamFluid), "steam"))
  }
}