/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/generators/master/MMPL-1.0.txt
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

}
