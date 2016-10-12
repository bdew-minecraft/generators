/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.dataport

import net.bdew.generators.controllers.steam.TileSteamTurbineController
import net.bdew.lib.computers.Result

object SteamTurbineCommands extends BaseCommands[TileSteamTurbineController] with CommandsPowered with CommandsOutputs with CommandsControl {
  command("getType", direct = true) { ctx =>
    "steam_turbine"
  }

  command("getInfo", direct = true) { ctx =>
    val core = getCore(ctx)
    Result.Map(
      "turbines" -> core.numTurbines.value,
      "rpm" -> core.speed.value,
      "production_max" -> core.maxMJPerTick.value,
      "production_current" -> core.outputAverage.average,
      "steam_usage" -> core.steamAverage.average,
      "steam_stored" -> core.steam.getFluidAmount,
      "steam_capacity" -> core.steam.getCapacity,
      "energy_stored" -> core.power.stored,
      "energy_capacity" -> core.power.capacity
    )
  }
}
