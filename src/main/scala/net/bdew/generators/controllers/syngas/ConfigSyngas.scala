package net.bdew.generators.controllers.syngas

import net.bdew.generators.registries.Modules
import net.bdew.lib.multiblock.{ModuleType, MultiblockMachineConfig}
import net.minecraftforge.common.ForgeConfigSpec

class ConfigSyngas(spec: ForgeConfigSpec.Builder) extends MultiblockMachineConfig(spec, Modules) {
  override def defaultRequired: Map[ModuleType, Int] = Map(
    Modules.mixingChamber -> 1,
  )

  override def defaultModules: Map[ModuleType, Int] = Map(
    Modules.itemInput -> 5,
    Modules.fluidInput -> 5,
    Modules.fluidOutput -> 6,
    Modules.heatingChamber -> 10,
    Modules.mixingChamber -> 25,
    Modules.sensor -> 10,
    Modules.control -> 10,
  )

  val internalTankCapacity: () => Int = intVal(spec, "InternalTankCapacity", "Internal steam/water/syngas/carbon capacity", 32000, 1)

  val workHeat: () => Double = doubleVal(spec, "WorkHeat", "Heat level required for conversion to start", 150, 1)
  val maxHeat: () => Double = doubleVal(spec, "MaxHeat", "Maximum heat level", 200, 1)

  val carbonPerMBSyngas: () => Double = doubleVal(spec, "CarbonPerMBSyngas", "Carbon consumed to produce 1mB of syngas", 50, 0.01D)
  val steamPerMBSyngas: () => Double = doubleVal(spec, "SteamPerMBSyngas", "Steam consumed to produce 1mB of syngas", 10, 0.01D)

  val waterSteamRatio: () => Double = doubleVal(spec, "WaterSteamRatio", "Water to steam conversion ratio", 3, 0.01D)

  val mixingChamberThroughput: () => Double = doubleVal(spec, "MixingChamberThroughput", "Syngas mB per tick", 1, 0.01D)
  val heatingChamberThroughput: () => Double = doubleVal(spec, "HeatingChamberThroughput", "Steam mB per tick", 50, 0.01D)
  val heatingChamberHeating: () => Double = doubleVal(spec, "HeatingChamberHeating", "Heat Units per tick", 0.1D, 0.001D)
  val heatingChamberLoss: () => Double = doubleVal(spec, "HeatingChamberLoss", "Heat Units per tick", 0.02D, 0.001D)

  val carbonPerHeat: () => Double = doubleVal(spec, "CarbonPerHeat", "Carbon consumed per Heat Unit produced", 20, 0.1D)
}
