/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.dataport

import net.bdew.generators.controllers.exchanger.TileExchangerController
import net.bdew.lib.computers.Result

object HeatExchangerCommands extends BaseCommands[TileExchangerController] with CommandsOutputs with CommandsControl {
  command("getType", direct = true) { ctx =>
    "heat_exchanger"
  }

  command("getInfo", direct = true) { ctx =>
    val core = getCore(ctx)
    Result.Map(
      "heat_current" -> core.heat.value,
      "heat_transfer_max" -> core.maxHeatTransfer.value,
      "heat_loss" -> core.heatLoss.average,
      "input_rate" -> core.inputRate.average,
      "output_rate" -> core.outputRate.average,
      "input_hot" -> resourceInfo(core.heaterIn),
      "input_cold" -> resourceInfo(core.coolerIn),
      "output_hot" -> resourceInfo(core.coolerOut),
      "output_cold" -> resourceInfo(core.heaterOut)
    )
  }
}
