/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.config

import java.io.File

import net.bdew.generators.Generators
import net.bdew.lib.gui.GuiHandler
import net.minecraftforge.common.config.Configuration

object Config {
  val guiHandler = new GuiHandler

  var powerShowUnits = "MJ"
  var powerShowMultiplier = 1F

  def load() {
    val c = new Configuration(new File(Generators.configDir, "client.config"))
    c.load()

    try {
      powerShowUnits = c.get("Display", "PowerShowUnits", "RF", "Units to use when displaying power. Valid values: MJ, EU, RF").getString
      if (powerShowUnits != "MJ") powerShowMultiplier = Tuning.getSection("Power").getFloat(powerShowUnits + "_MJ_Ratio")
    } finally {
      c.save()
    }

    Items.load()
    Blocks.load()
    Machines.load()
  }
}