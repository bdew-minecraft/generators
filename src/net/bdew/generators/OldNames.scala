/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators

import java.util.Locale

import net.bdew.generators.config.{CapacitorMaterials, Items, TurbineMaterials}
import net.bdew.generators.controllers.exchanger.BlockExchangerController
import net.bdew.generators.controllers.steam.BlockSteamTurbineController
import net.bdew.generators.controllers.syngas.BlockSyngasController
import net.bdew.generators.controllers.turbine.BlockTurbineController
import net.bdew.generators.modules.control.BlockControl
import net.bdew.generators.modules.efficiency.{BlockEfficiencyUpgradeTier1, BlockEfficiencyUpgradeTier2}
import net.bdew.generators.modules.exchanger.BlockExchanger
import net.bdew.generators.modules.fluidInput.BlockFluidInput
import net.bdew.generators.modules.fluidOutputSelect.BlockFluidOutputSelect
import net.bdew.generators.modules.forgeOutput.BlockForgeOutput
import net.bdew.generators.modules.fuelTank.BlockFuelTank
import net.bdew.generators.modules.heatingChamber.BlockHeatingChamber
import net.bdew.generators.modules.itemInput.BlockItemInput
import net.bdew.generators.modules.itemOutput.BlockItemOutput
import net.bdew.generators.modules.mixingChamber.BlockMixingChamber
import net.bdew.generators.modules.pressure.{BlockPressureInput, BlockPressureOutput}
import net.bdew.generators.modules.rfOutput.BlockRfOutput
import net.bdew.generators.modules.sensor.BlockSensor
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent.MissingMapping
import net.minecraftforge.fml.common.registry.GameRegistry.Type
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry

object OldNames {
  def combineOpts[T](x: Option[T]*): List[T] = x.flatten.toList

  val turbineMap = {
    (for ((name, mat) <- TurbineMaterials.registry) yield
      combineOpts(
        mat.bladeItem map (s"TurbineBlade$name" -> _),
        mat.rotorItem map (s"TurbineRotor$name" -> _),
        mat.kitItem map (s"TurbineKit$name" -> _),
        mat.turbineBlock map (s"Turbine$name" -> _))
      ).flatten.toMap
  }

  val capacitorMap = {
    (for ((name, mat) <- CapacitorMaterials.registry) yield
      combineOpts(
        mat.kitItem map (s"CapacitorKit$name" -> _),
        mat.capacitorBlock map (s"PowerCapacitor$name" -> _))
      ).flatten.toMap
  }

  val map: Map[String, IForgeRegistryEntry[_]] = Map(
    "IronFrame" -> Items.ironFrame,
    "PowerIO" -> Items.powerIO,
    "IronTubing" -> Items.ironTubing,
    "IronWiring" -> Items.ironWiring,
    "Controller" -> Items.controller,
    "PressureValve" -> Items.pressureValve,
    "AdvancedPressureValve" -> Items.advancedPressureValve,
    "UpgradeKit" -> Items.upgradeKit,

    "TurbineController" -> BlockTurbineController,
    "ExchangerController" -> BlockExchangerController,
    "SteamTurbineController" -> BlockSteamTurbineController,
    "SyngasController" -> BlockSyngasController,
    "RFOutput" -> BlockRfOutput,
    "ForgeOutput" -> BlockForgeOutput,
    "FluidInput" -> BlockFluidInput,
    "FluidOutputSelect" -> BlockFluidOutputSelect,
    "ItemInput" -> BlockItemInput,
    "ItemOutput" -> BlockItemOutput,
    "FuelTank" -> BlockFuelTank,
    "HeatingChamber" -> BlockHeatingChamber,
    "MixingChamber" -> BlockMixingChamber,
    "HeatExchanger" -> BlockExchanger,
    "Sensor" -> BlockSensor,
    "Control" -> BlockControl,
    "EfficiencyUpgradeTier1" -> BlockEfficiencyUpgradeTier1,
    "EfficiencyUpgradeTier2" -> BlockEfficiencyUpgradeTier2,
    "PressureInput" -> BlockPressureInput,
    "PressureOutputSelect" -> BlockPressureOutput
  ) ++ capacitorMap ++ turbineMap

  val lowerMap: Map[String, IForgeRegistryEntry[_]] = map.map(x => "advgenerators:" + x._1.toLowerCase(Locale.US) -> x._2).toMap

  def checkRemap(mapping: MissingMapping): Unit = {
    lowerMap.get(mapping.name) match {
      case Some(x: Block) if mapping.`type` == Type.BLOCK => mapping.remap(x)
      case Some(x: Block) if mapping.`type` == Type.ITEM => mapping.remap(Item.getItemFromBlock(x))
      case Some(x: Item) if mapping.`type` == Type.ITEM => mapping.remap(x)
      case _ => //nothing
    }
  }
}
