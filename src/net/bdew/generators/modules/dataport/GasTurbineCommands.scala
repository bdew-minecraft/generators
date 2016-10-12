/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.dataport

import net.bdew.generators.config.TurbineFuel
import net.bdew.generators.controllers.turbine.TileTurbineController
import net.bdew.lib.computers.Result

object GasTurbineCommands extends BaseCommands[TileTurbineController] with CommandsPowered with CommandsOutputs with CommandsControl {
  command("getType", direct = true) { ctx =>
    "gas_turbine"
  }

  command("getInfo", direct = true) { ctx =>
    val core = getCore(ctx)
    Result.Map(
      "turbines" -> core.numTurbines.value,
      "production_max" -> core.maxMJPerTick.value,
      "production_current" -> core.outputAverage.average,
      "fuel_consumed_max" -> core.fuelPerTick.value,
      "fuel_consumed_current" -> core.fuelPerTickAverage.average,
      "fuel_efficiency" -> core.fuelEfficiency.value,
      "fuel_energy" -> (if (core.fuel.getFluidAmount > 0) TurbineFuel.getFuelValue(core.fuel.getFluid.getFluid) * core.fuelEfficiency else 0f),
      "energy_stored" -> core.power.stored,
      "energy_capacity" -> core.power.capacity,
      "fuel" -> tankInfo(core.fuel)
    )
  }
}
