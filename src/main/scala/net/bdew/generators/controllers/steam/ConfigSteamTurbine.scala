package net.bdew.generators.controllers.steam

import net.bdew.generators.registries.Modules
import net.bdew.lib.multiblock.{ModuleType, MultiblockMachineConfig}
import net.minecraftforge.common.ForgeConfigSpec

class ConfigSteamTurbine(spec: ForgeConfigSpec.Builder) extends MultiblockMachineConfig(spec, Modules) {
  override def defaultRequired: Map[ModuleType, Int] = Map(
    Modules.turbine -> 1,
  )

  override def defaultModules: Map[ModuleType, Int] = Map(
    Modules.powerOutput -> 6,
    Modules.turbine -> 50,
    Modules.fluidInput -> 5,
    Modules.powerCapacitor -> 10,
    Modules.sensor -> 10,
    Modules.control -> 10,
  )

  spec.comment("Base RPM delta is capped to MaxRPM * InertiaFunctionMultiplier * e^(InertiaFunctionExponent * CurrentRPM / MaxRPM)").push("Inertia")

  val inertiaFunctionMultiplier: () => Double = doubleVal(spec, "InertiaFunctionMultiplier", null, 0.025)
  val inertiaFunctionExponent: () => Double = doubleVal(spec, "InertiaFunctionExponent", null, -2)

  spec.pop()

  spec.comment("SpinUpMultiplier must be > BaseDragMultiplier + CoilDragMultiplier").push("Drag")

  val spinUpMultiplier: () => Double =
    doubleVal(spec, "SpinUpMultiplier", "Affects how fast a turbine spins up to it's target RPM", 1)

  val baseDragMultiplier: () => Double =
    doubleVal(spec, "BaseDragMultiplier", "Base constant RPM loss", 0.2)

  val coilDragMultiplier: () => Double =
    doubleVal(spec, "CoilDragMultiplier", "RPM loss when generating power", 0.75)

  spec.pop()

  val maxRPM: () => Double = doubleVal(spec, "MaxRPM", "Maximum rotation speed", 5000)

  val steamEnergyDensity: () => Double = doubleVal(spec, "SteamEnergyDensity", "FE/mB", 2)

  val internalSteamCapacity: () => Int =
    intVal(spec, "InternalSteamCapacity", "Internal steam capacity (mB)", 16000, 1)

  val internalEnergyCapacity: () => Int =
    intVal(spec, "InternalEnergyCapacity",
      "Internal energy capacity (FE) - this is the base value, expandable by capacitors",
      100000, 1)
}
