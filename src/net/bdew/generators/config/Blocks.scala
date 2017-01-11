/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.config

import net.bdew.generators.blocks.{BlockSteam, BlockSyngas}
import net.bdew.generators.compat.PowerProxy
import net.bdew.generators.modules.control.BlockControl
import net.bdew.generators.modules.dataport.BlockDataPort
import net.bdew.generators.modules.efficiency.{BlockEfficiencyUpgradeTier1, BlockEfficiencyUpgradeTier2}
import net.bdew.generators.modules.euOutput._
import net.bdew.generators.modules.exchanger.BlockExchanger
import net.bdew.generators.modules.fluidInput.BlockFluidInput
import net.bdew.generators.modules.fluidOutputSelect.BlockFluidOutputSelect
import net.bdew.generators.modules.forgeOutput.BlockForgeOutput
import net.bdew.generators.modules.fuelTank.BlockFuelTank
import net.bdew.generators.modules.heatingChamber.BlockHeatingChamber
import net.bdew.generators.modules.itemInput.BlockItemInput
import net.bdew.generators.modules.itemOutput.BlockItemOutput
import net.bdew.generators.modules.mixingChamber.BlockMixingChamber
import net.bdew.generators.modules.powerCapacitor.TilePowerCapacitor
import net.bdew.generators.modules.pressure.{BlockPressureInput, BlockPressureOutput}
import net.bdew.generators.modules.rfOutput.BlockRfOutput
import net.bdew.generators.modules.sensor.BlockSensor
import net.bdew.generators.modules.teslaOutput.BlockTeslaOutput
import net.bdew.generators.modules.turbine.TileTurbine
import net.bdew.generators.{CreativeTabsGenerators, Generators}
import net.bdew.lib.Misc
import net.bdew.lib.config.BlockManager
import net.bdew.lib.render.FluidModelUtils
import net.bdew.pressure.api.PressureAPI
import net.minecraft.item.EnumRarity
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fluids.{BlockFluidBase, Fluid, FluidRegistry}
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.registry.GameRegistry

object Blocks extends BlockManager(CreativeTabsGenerators.main) {
  if (PowerProxy.haveRF && PowerProxy.RFEnabled)
    regBlock(BlockRfOutput)

  if (PowerProxy.haveIC2 && PowerProxy.EUEnabled) {
    regBlock(BlockEuOutputLV)
    regBlock(BlockEuOutputMV)
    regBlock(BlockEuOutputHV)
    regBlock(BlockEuOutputEV)
    regBlock(BlockEuOutputIV)
  }

  if (PowerProxy.haveTesla && PowerProxy.TeslaEnabled) {
    regBlock(BlockTeslaOutput)
  }

  if (PowerProxy.ForgeEnabled)
    regBlock(BlockForgeOutput)

  //  if (Misc.haveModVersion("MekanismAPI|gas@[9.0.0,)")) {
  //    regBlock(BlockGasInput)
  //  }

  regBlock(BlockFluidInput)
  regBlock(BlockFluidOutputSelect)

  regBlock(BlockItemInput)
  regBlock(BlockItemOutput)

  GameRegistry.registerTileEntity(classOf[TileTurbine], "advgenerators.Turbine")
  GameRegistry.registerTileEntity(classOf[TilePowerCapacitor], "advgenerators.PowerCapacitor")

  regBlock(BlockFuelTank)
  regBlock(BlockHeatingChamber)
  regBlock(BlockMixingChamber)
  regBlock(BlockExchanger)

  regBlock(BlockSensor)
  regBlock(BlockControl)

  regBlock(BlockEfficiencyUpgradeTier1)
  regBlock(BlockEfficiencyUpgradeTier2)

  object PressureBlocks {
    def init() {
      if (PressureAPI.HELPER != null) {
        Generators.logInfo("Pressure pipes detected (%s), adding pressure modules", PressureAPI.HELPER)
        regBlock(BlockPressureInput)
        regBlock(BlockPressureOutput)
      }
    }
  }

  if (Misc.haveModVersion("pressure")) PressureBlocks.init()

  if (Misc.haveModVersion("ComputerCraft") || Misc.haveModVersion("OpenComputers")) {
    regBlock(BlockDataPort)
  }

  regFluid("steam", new BlockSteam(_)) {
    _.setTemperature(1000)
      .setGaseous(true)
      .setLuminosity(0)
      .setRarity(EnumRarity.COMMON)
      .setDensity(-10)
  }

  regFluid("syngas", new BlockSyngas(_)) {
    _.setTemperature(1000)
      .setGaseous(true)
      .setLuminosity(0)
      .setRarity(EnumRarity.COMMON)
      .setDensity(-10)
  }

  // Can't store them before a world is loaded, because of forge multiple fluids of the same type nonsense
  def syngasFluid = FluidRegistry.getFluid("syngas")
  def steamFluid = FluidRegistry.getFluid("steam")

  def regFluid(name: String, block: (Fluid) => BlockFluidBase)(params: (Fluid) => Unit): Fluid = {
    val fluid = if (!FluidRegistry.isFluidRegistered(name)) {
      Generators.logInfo("Fluid '%s' not registered by any other mod, creating...", name)
      val newFluid = new Fluid(name, new ResourceLocation(Generators.modId, "blocks/" + name + "/still"), new ResourceLocation(Generators.modId, "blocks/" + name + "/flowing"))
      params.apply(newFluid)
      FluidRegistry.registerFluid(newFluid)
      newFluid
    } else FluidRegistry.getFluid(name)

    if (fluid.getBlock == null) {
      Generators.logInfo("Adding block for fluid '%s'", name)
      val newBlock = regBlock(block(fluid))
      fluid.setBlock(newBlock)

      if (FMLCommonHandler.instance().getSide.isClient)
        FluidModelUtils.registerFluidModel(newBlock, Generators.modId + ":fluids")
    }

    fluid
  }
}