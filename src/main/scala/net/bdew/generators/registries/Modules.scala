package net.bdew.generators.registries

import net.bdew.lib.multiblock.{ModuleType, ModuleTypes}

object Modules extends ModuleTypes {
  val turbine: ModuleType = define("Turbine")
  val fuelTank: ModuleType = define("FuelTank")
  val powerCapacitor: ModuleType = define("PowerCapacitor")
  val powerOutput: ModuleType = define("PowerOutput")
  val fluidOutput: ModuleType = define("FluidOutputSelect")
  val fluidInput: ModuleType = define("FluidInput")
  val itemInput: ModuleType = define("ItemInput")
  val itemOutput: ModuleType = define("ItemOutput")
  val sensor: ModuleType = define("Sensor")
  val control: ModuleType = define("Control")
  val efficiencyUpgradeTier1: ModuleType = define("EfficiencyUpgradeTier1")
  val efficiencyUpgradeTier2: ModuleType = define("EfficiencyUpgradeTier2")
  val heatingChamber: ModuleType = define("HeatingChamber")
  val mixingChamber: ModuleType = define("MixingChamber")
  val heatExchanger: ModuleType = define("HeatExchanger")
}
