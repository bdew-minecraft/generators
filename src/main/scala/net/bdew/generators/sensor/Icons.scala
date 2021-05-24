package net.bdew.generators.sensor

import net.bdew.generators.Generators
import net.bdew.lib.gui.Texture
import net.bdew.lib.render.IconPreloader
import net.minecraftforge.api.distmarker.{Dist, OnlyIn}
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus

@Mod.EventBusSubscriber(value = Array(Dist.CLIENT), modid = Generators.ModId, bus = Bus.MOD)
object Icons extends IconPreloader {

  trait Loader {
    def iconName: String
    @OnlyIn(Dist.CLIENT)
    def texture: Texture = map(iconName)
  }

  val clear: Icons.TextureLoc = TextureLoc("advgenerators:sensor/null")
  val disabled: Icons.TextureLoc = TextureLoc("advgenerators:sensor/disabled")
  val power: Icons.TextureLoc = TextureLoc("advgenerators:sensor/power")

  val fuelTank: Icons.TextureLoc = TextureLoc("advgenerators:sensor/tank/fuel")
  val steamTank: Icons.TextureLoc = TextureLoc("advgenerators:sensor/tank/steam")
  val carbonTank: Icons.TextureLoc = TextureLoc("advgenerators:sensor/tank/carbon")
  val waterTank: Icons.TextureLoc = TextureLoc("advgenerators:sensor/tank/water")

  val hotIn: Icons.TextureLoc = TextureLoc("advgenerators:sensor/tank/hot_in")
  val hotOut: Icons.TextureLoc = TextureLoc("advgenerators:sensor/tank/hot_out")
  val coldIn: Icons.TextureLoc = TextureLoc("advgenerators:sensor/tank/cold_in")
  val coldOut: Icons.TextureLoc = TextureLoc("advgenerators:sensor/tank/cold_out")

  val fillEmpty: Icons.TextureLoc = TextureLoc("advgenerators:sensor/fill/empty")
  val fillFull: Icons.TextureLoc = TextureLoc("advgenerators:sensor/fill/full")
  val fillNotEmpty: Icons.TextureLoc = TextureLoc("advgenerators:sensor/fill/not_empty")
  val fillNotFull: Icons.TextureLoc = TextureLoc("advgenerators:sensor/fill/not_full")
  val fill5: Icons.TextureLoc = TextureLoc("advgenerators:sensor/fill/5")
  val fill25: Icons.TextureLoc = TextureLoc("advgenerators:sensor/fill/25")
  val fill50: Icons.TextureLoc = TextureLoc("advgenerators:sensor/fill/50")
  val fill75: Icons.TextureLoc = TextureLoc("advgenerators:sensor/fill/75")
  val fill95: Icons.TextureLoc = TextureLoc("advgenerators:sensor/fill/95")

  val turbine: Icons.TextureLoc = TextureLoc("advgenerators:sensor/turbine/turbine")
  val turbineStop: Icons.TextureLoc = TextureLoc("advgenerators:sensor/turbine/stopped")
  val turbineLow: Icons.TextureLoc = TextureLoc("advgenerators:sensor/turbine/low")
  val turbineMed: Icons.TextureLoc = TextureLoc("advgenerators:sensor/turbine/med")
  val turbineHigh: Icons.TextureLoc = TextureLoc("advgenerators:sensor/turbine/high")

  val heat: Icons.TextureLoc = TextureLoc("advgenerators:sensor/heat/heat")
  val heatCold: Icons.TextureLoc = TextureLoc("advgenerators:sensor/heat/cold")
  val heatLow: Icons.TextureLoc = TextureLoc("advgenerators:sensor/heat/low")
  val heatMed: Icons.TextureLoc = TextureLoc("advgenerators:sensor/heat/med")
  val heatHigh: Icons.TextureLoc = TextureLoc("advgenerators:sensor/heat/high")
}
