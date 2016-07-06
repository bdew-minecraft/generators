/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.control

import net.bdew.lib.gui.Texture
import net.bdew.lib.render.IconPreloader
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

object Icons extends IconPreloader {

  trait Loader {
    def iconName: String
    @SideOnly(Side.CLIENT)
    def texture: Texture = map(iconName)
  }

  val disabled = TextureLoc("advgenerators:control/disabled")
  val steam = TextureLoc("advgenerators:control/steam")
  val fuel = TextureLoc("advgenerators:control/fuel")
  val energy = TextureLoc("advgenerators:control/energy")
  val exchange = TextureLoc("advgenerators:control/exchange")
  val heating = TextureLoc("advgenerators:control/heating")
  val mixing = TextureLoc("advgenerators:control/mix")
}
