/*
 * Copyright (c) bdew, 2014
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
  val rotor = regSimpleItem("TurbineRotor")

  regSimpleItem("TurbineBlade")
  regSimpleItem("IronFrame")
  regSimpleItem("PowerIO")
  regSimpleItem("IronTubing")
  regSimpleItem("IronWiring")
  regSimpleItem("Controller")
}