package net.bdew.generators.modules.exchanger

import net.bdew.lib.config.ConfigSection
import net.minecraftforge.common.ForgeConfigSpec

class ConfigExchanger(spec: ForgeConfigSpec.Builder, defaultHeatTransfer: Double) extends ConfigSection {
  val heatTransfer: () => Double = doubleVal(spec, "HeatTransfer", "Heat Units per tick", defaultHeatTransfer)
}
