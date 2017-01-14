/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.config

import net.bdew.generators.CreativeTabsGenerators
import net.bdew.lib.config.ItemManager

object Items extends ItemManager(CreativeTabsGenerators.main) {
  val ironFrame = regSimpleItem("iron_frame")
  val powerIO = regSimpleItem("power_io")
  val ironTubing = regSimpleItem("iron_tubing")
  val ironWiring = regSimpleItem("iron_wiring")
  val controller = regSimpleItem("controller")
  val pressureValve = regSimpleItem("pressure_valve")
  val advancedPressureValve = regSimpleItem("advanced_pressure_valve")
  val upgradeKit = regSimpleItem("upgrade_kit")
}
