/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.dataport

import net.bdew.generators.controllers.syngas.TileSyngasController
import net.bdew.lib.computers.Result

object SyngasProducerCommands extends BaseCommands[TileSyngasController] with CommandsOutputs with CommandsControl {
  command("getType", direct = true) { ctx =>
    "syngas_producer"
  }

  command("getInfo", direct = true) { ctx =>
    val core = getCore(ctx)
    Result.Map(
      "heat_current" -> core.heat.value,
      "heat_average" -> core.avgHeatDelta.average,
      "carbon_consumed" -> core.avgCarbonUsed.average,
      "syngas_produced" -> core.avgSyngasProduced.average,
      "water" -> tankInfo(core.waterTank),
      "syngas" -> tankInfo(core.syngasTank),
      "carbon_stored" -> core.carbonBuffer.value,
      "carbon_max" -> core.cfg.internalTankCapacity,
      "syngas_stored" -> core.steamBuffer.value,
      "syngas_max" -> core.cfg.internalTankCapacity
    )
  }
}
