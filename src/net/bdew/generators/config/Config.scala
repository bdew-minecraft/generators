/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.config

import java.io.File

import net.bdew.lib.gui.GuiHandler
import net.minecraftforge.common.config.Configuration

object Config {
  val guiHandler = new GuiHandler

  var powerShowUnits = "MJ"
  var powerShowMultiplier = 1F

  var alwaysShowFuelTooltip = true
  var alwaysShowCarbonTooltip = true

  def load(config: File) {
    val c = new Configuration(config)
    c.load()

    try {
      powerShowUnits = c.get("Display", "PowerShowUnits", "RF", "Units to use when displaying power. Valid values: MJ, EU, RF, T", Array("MJ", "EU", "RF", "T")).getString
      if (powerShowUnits != "MJ") powerShowMultiplier = Tuning.getSection("Power").getFloat(powerShowUnits + "_MJ_Ratio")

      alwaysShowFuelTooltip = c.get("Tooltips", "AlwaysShowFuelTooltip", true, "If false will only show tooltip in Turbine GUI").getBoolean
      alwaysShowCarbonTooltip = c.get("Tooltips", "AlwaysShowCarbonTooltip", true, "If false will only show tooltip in Syngas Producer GUI").getBoolean
    } finally {
      c.save()
    }
  }
}