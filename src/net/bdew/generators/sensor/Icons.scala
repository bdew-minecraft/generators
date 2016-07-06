/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.sensor

import net.bdew.lib.gui.Texture
import net.bdew.lib.render.IconPreloader
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

object Icons extends IconPreloader {

  trait Loader {
    def iconName: String
    @SideOnly(Side.CLIENT)
    def texture: Texture = map(iconName)
  }

  val clear = TextureLoc("advgenerators:sensor/null")
  val disabled = TextureLoc("advgenerators:sensor/disabled")
  val power = TextureLoc("advgenerators:sensor/power")

  val fuelTank = TextureLoc("advgenerators:sensor/tank/fuel")
  val steamTank = TextureLoc("advgenerators:sensor/tank/steam")
  val carbonTank = TextureLoc("advgenerators:sensor/tank/carbon")
  val waterTank = TextureLoc("advgenerators:sensor/tank/water")

  val hotIn = TextureLoc("advgenerators:sensor/tank/hot_in")
  val hotOut = TextureLoc("advgenerators:sensor/tank/hot_out")
  val coldIn = TextureLoc("advgenerators:sensor/tank/cold_in")
  val coldOut = TextureLoc("advgenerators:sensor/tank/cold_out")

  val fillEmpty = TextureLoc("advgenerators:sensor/fill/empty")
  val fillFull = TextureLoc("advgenerators:sensor/fill/full")
  val fillNotEmpty = TextureLoc("advgenerators:sensor/fill/not_empty")
  val fillNotFull = TextureLoc("advgenerators:sensor/fill/not_full")
  val fill5 = TextureLoc("advgenerators:sensor/fill/5")
  val fill25 = TextureLoc("advgenerators:sensor/fill/25")
  val fill50 = TextureLoc("advgenerators:sensor/fill/50")
  val fill75 = TextureLoc("advgenerators:sensor/fill/75")
  val fill95 = TextureLoc("advgenerators:sensor/fill/95")

  val turbine = TextureLoc("advgenerators:sensor/turbine/turbine")
  val turbineStop = TextureLoc("advgenerators:sensor/turbine/stopped")
  val turbineLow = TextureLoc("advgenerators:sensor/turbine/low")
  val turbineMed = TextureLoc("advgenerators:sensor/turbine/med")
  val turbineHigh = TextureLoc("advgenerators:sensor/turbine/high")

  val heat = TextureLoc("advgenerators:sensor/heat/heat")
  val heatCold = TextureLoc("advgenerators:sensor/heat/cold")
  val heatLow = TextureLoc("advgenerators:sensor/heat/low")
  val heatMed = TextureLoc("advgenerators:sensor/heat/med")
  val heatHigh = TextureLoc("advgenerators:sensor/heat/high")
}
