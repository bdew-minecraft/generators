/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators

import net.bdew.generators.config.TurbineMaterials
import net.bdew.lib.CreativeTabContainer

object CreativeTabsGenerators extends CreativeTabContainer {
  val main = new Tab("bdew.generators", TurbineMaterials.registry("Iron").rotorItem.get)
}
