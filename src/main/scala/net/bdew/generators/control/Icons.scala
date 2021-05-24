package net.bdew.generators.control

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

  val disabled: Icons.TextureLoc = TextureLoc("advgenerators:control/disabled")
  val steam: Icons.TextureLoc = TextureLoc("advgenerators:control/steam")
  val fuel: Icons.TextureLoc = TextureLoc("advgenerators:control/fuel")
  val energy: Icons.TextureLoc = TextureLoc("advgenerators:control/energy")
  val exchange: Icons.TextureLoc = TextureLoc("advgenerators:control/exchange")
  val heating: Icons.TextureLoc = TextureLoc("advgenerators:control/heating")
  val mixing: Icons.TextureLoc = TextureLoc("advgenerators:control/mix")
}
