/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.config

object Modules {
  lazy val cfg = Tuning.getSection("Modules")

  object FuelTank {
    lazy val cfg = Modules.cfg.getSection("FuelTank")
    lazy val capacity = cfg.getInt("Capacity")
  }

  object PowerCapacitor {
    lazy val cfg = Modules.cfg.getSection("PowerCapacitor")
    lazy val capacity = cfg.getInt("Capacity")
  }

  object HeatExchanger {
    lazy val cfg = Modules.cfg.getSection("HeatExchanger")
    lazy val heatTransfer = cfg.getInt("HeatTransfer")
  }

}
