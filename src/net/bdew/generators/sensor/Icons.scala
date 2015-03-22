/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.sensor

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.bdew.lib.gui.Texture
import net.bdew.lib.render.IconPreloader

object Icons extends IconPreloader(1) {

  trait Loader {
    def iconName: String
    @SideOnly(Side.CLIENT)
    def texture: Texture = map(iconName)
  }

  val clear = TextureLoc("advgenerators:sensor/null")
  val disabled = TextureLoc("advgenerators:sensor/disabled")
  val fuelTank = TextureLoc("advgenerators:sensor/fueltank")
  val steamTank = TextureLoc("advgenerators:sensor/steamtank")
  val hotIn = TextureLoc("advgenerators:sensor/hot_in")
  val hotOut = TextureLoc("advgenerators:sensor/hot_out")
  val coldIn = TextureLoc("advgenerators:sensor/cold_in")
  val coldOut = TextureLoc("advgenerators:sensor/cold_out")
  val power = TextureLoc("advgenerators:sensor/power")
  val fillEmpty = TextureLoc("advgenerators:sensor/fill_0")
  val fillFull = TextureLoc("advgenerators:sensor/fill_100")
  val fillNotEmpty = TextureLoc("advgenerators:sensor/fill_not_empty")
  val fillNotFull = TextureLoc("advgenerators:sensor/fill_not_full")
  val fill25 = TextureLoc("advgenerators:sensor/fill_25")
  val fill50 = TextureLoc("advgenerators:sensor/fill_50")
  val fill75 = TextureLoc("advgenerators:sensor/fill_75")
}
