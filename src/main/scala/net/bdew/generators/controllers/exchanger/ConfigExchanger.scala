package net.bdew.generators.controllers.exchanger

import net.bdew.generators.registries.Modules
import net.bdew.lib.multiblock.{ModuleType, MultiblockMachineConfig}
import net.minecraftforge.common.ForgeConfigSpec

class ConfigExchanger(spec: ForgeConfigSpec.Builder) extends MultiblockMachineConfig(spec, Modules) {
  override def defaultRequired: Map[ModuleType, Int] = Map(
    Modules.heatExchanger -> 1,
  )

  override def defaultModules: Map[ModuleType, Int] = Map(
    Modules.itemInput -> 5,
    Modules.fluidInput -> 5,
    Modules.itemOutput -> 6,
    Modules.fluidOutput -> 6,
    Modules.heatExchanger -> 50,
    Modules.sensor -> 10,
    Modules.control -> 10,
  )

  val internalTankCapacity: () => Int = intVal(spec, "InternalTankCapacity", null, 16000)
  val maxHeat: () => Double = doubleVal(spec, "MaxHeat", "Should be > StartHeating + (max HeatTransfer), otherwise weirdness will ensue", 1000)
  val startHeating: () => Double = doubleVal(spec, "StartHeating", "Start heating above this temperature", 150)
  val heatDecay: () => Double = doubleVal(spec, "HeatDecay", "Decay per tick", 0.05)
}
