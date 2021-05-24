package net.bdew.generators.controllers.turbine

import net.bdew.generators.registries.Modules
import net.bdew.lib.multiblock.{ModuleType, MultiblockMachineConfig}
import net.minecraftforge.common.ForgeConfigSpec

class ConfigFuelTurbine(spec: ForgeConfigSpec.Builder) extends MultiblockMachineConfig(spec, Modules) {
  override def defaultRequired: Map[ModuleType, Int] = Map(
    Modules.turbine -> 1,
  )

  override def defaultModules: Map[ModuleType, Int] = Map(
    Modules.powerOutput -> 6,
    Modules.turbine -> 50,
    Modules.fluidInput -> 5,
    Modules.fuelTank -> 10,
    Modules.powerCapacitor -> 10,
    Modules.sensor -> 10,
    Modules.control -> 10,
    Modules.efficiencyUpgradeTier1 -> 1,
    Modules.efficiencyUpgradeTier2 -> 1,
  )

  val internalFuelCapacity: () => Int =
    intVal(spec, "InternalFuelCapacity",
      "Internal fuel capacity (mB) - this is the base value, expandable by tanks",
      1000, 1
    )

  val internalEnergyCapacity: () => Int =
    intVal(spec, "InternalEnergyCapacity",
      "Internal energy capacity (FE) - this is the base value, expandable by capacitors",
      100000, 1)

  spec.comment("Fuel Efficiency").push("FuelEfficiency")

  val fuelEfficiencyBase: () => Float = floatVal(spec, "Base", "Base value", 1.1F, 0F, 100F)
  val fuelEfficiencyTier1: () => Float = floatVal(spec, "Tier1", "With tier 1 upgrade", 1.35F, 0F, 100F)
  val fuelEfficiencyTier2: () => Float = floatVal(spec, "Tier2", "With tier 2 upgrade", 1.85F, 0F, 100F)

  spec.pop()
}
