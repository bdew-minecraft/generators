package net.bdew.generators.integration

import net.bdew.generators.integration.ic2c.{IC2CIntegration, IC2Config}
import net.bdew.lib.config.ConfigSection
import net.minecraftforge.common.ForgeConfigSpec

class IntegrationConfig(spec: ForgeConfigSpec.Builder) extends ConfigSection {
  val IC2: IC2Config = if (IC2CIntegration.isAvailable) ConfigSection(spec, "IC2C", new IC2Config(spec)) else null
}
