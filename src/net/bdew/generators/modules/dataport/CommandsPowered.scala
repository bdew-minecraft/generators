/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.dataport

import net.bdew.generators.config.Tuning
import net.bdew.generators.controllers.PoweredController
import net.bdew.lib.computers.PString

trait CommandsPowered {
  self: BaseCommands[_ <: PoweredController] =>
  def getPowerMultiplier(unit: Option[String]) = unit match {
    case None => 1.0f
    case Some("MJ") => 1.0f
    case Some(x) =>
      try {
        Tuning.getSection("Power").getFloat(x + "_MJ_Ratio")
      } catch {
        case _: Throwable => err("Unknown power unit")
      }
  }

  command("getEnergyStored", direct = true) { ctx =>
    val multiplier = getPowerMultiplier(ctx.params(PString.?))
    getCore(ctx).power.stored * multiplier
  }

  command("getEnergyCapacity", direct = true) { ctx =>
    val multiplier = getPowerMultiplier(ctx.params(PString.?))
    getCore(ctx).power.capacity * multiplier
  }
}
