package net.bdew.generators.modules.turbine

import net.bdew.lib.config.ConfigSection
import net.minecraftforge.common.ForgeConfigSpec

class ConfigTurbine(spec: ForgeConfigSpec.Builder, maxFEPerTickDefault: Int, inertiaDefault: Double) extends ConfigSection {
  val maxFEPerTick: () => Int = getter(spec.comment("Maximum FE generated per tick")
    .defineInRange("MaxFEPerTick", maxFEPerTickDefault, 1, Int.MaxValue), Int.unbox)
  val inertia: () => Double = getter(spec.comment("Maximum FE generated per tick")
    .defineInRange("Inertia", inertiaDefault, 0D, 100D), Double.unbox)
}
