package net.bdew.generators.config

import net.bdew.generators.modules.exchanger.ConfigExchanger
import net.bdew.generators.modules.fuelTank.ConfigFuelTank
import net.bdew.generators.modules.powerCapacitor.ConfigCapacitor
import net.bdew.generators.modules.turbine.ConfigTurbine
import net.bdew.lib.config.ConfigSection
import net.minecraftforge.common.ForgeConfigSpec

class ModulesConfig(spec: ForgeConfigSpec.Builder) {
  val fuelTank: ConfigFuelTank = ConfigSection(spec, "FuelTank", new ConfigFuelTank(spec, 16000))

  val heatExchanger: ConfigExchanger = ConfigSection(spec, "HeatExchanger", new ConfigExchanger(spec, 16))

  spec.comment("Turbine module configuration").push("Turbines")
  val turbineTier1: ConfigTurbine = ConfigSection(spec, "Tier1", "Basic Turbine", new ConfigTurbine(spec, 100, 1))
  val turbineTier2: ConfigTurbine = ConfigSection(spec, "Tier2", "Enhanced Turbine", new ConfigTurbine(spec, 150, 1.1))
  val turbineTier3: ConfigTurbine = ConfigSection(spec, "Tier3", "Advanced Turbine", new ConfigTurbine(spec, 250, 1.2))
  val turbineTier4: ConfigTurbine = ConfigSection(spec, "Tier4", "Reinforced Turbine", new ConfigTurbine(spec, 500, 1.25))
  val turbineTier5: ConfigTurbine = ConfigSection(spec, "Tier5", "Composite Turbine", new ConfigTurbine(spec, 1000, 1.4))
  spec.pop()

  spec.comment("Capacitor module configuration").push("Capacitors")
  val capacitorTier1: ConfigCapacitor = ConfigSection(spec, "Tier1", "Basic Capacitor", new ConfigCapacitor(spec, 1000000))
  val capacitorTier2: ConfigCapacitor = ConfigSection(spec, "Tier2", "Advanced Capacitor", new ConfigCapacitor(spec, 5000000))
  val capacitorTier3: ConfigCapacitor = ConfigSection(spec, "Tier3", "High Density Capacitor", new ConfigCapacitor(spec, 25000000))
  spec.pop()
}
