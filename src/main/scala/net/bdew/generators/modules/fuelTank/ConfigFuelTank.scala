package net.bdew.generators.modules.fuelTank

import net.bdew.lib.config.ConfigSection
import net.minecraftforge.common.ForgeConfigSpec

class ConfigFuelTank(spec: ForgeConfigSpec.Builder, defaultCapacity: Int) extends ConfigSection {
  val capacity: () => Int = intVal(spec, "Capacity", "Capacity (mB)", defaultCapacity)
}
