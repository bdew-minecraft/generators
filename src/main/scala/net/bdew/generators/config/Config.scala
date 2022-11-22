package net.bdew.generators.config

import net.bdew.generators.controllers.exchanger.ConfigExchanger
import net.bdew.generators.controllers.steam.ConfigSteamTurbine
import net.bdew.generators.controllers.syngas.ConfigSyngas
import net.bdew.generators.controllers.turbine.ConfigFuelTurbine
import net.bdew.generators.integration.IntegrationConfig
import net.bdew.lib.config.ConfigSection
import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.config.ModConfig

object Config {
  private val commonBuilder = new ForgeConfigSpec.Builder

  val FuelTurbine: ConfigFuelTurbine = ConfigSection(commonBuilder, "TurbineFuel", new ConfigFuelTurbine(commonBuilder))
  val SteamTurbine: ConfigSteamTurbine = ConfigSection(commonBuilder, "TurbineSteam", new ConfigSteamTurbine(commonBuilder))
  val SyngasProducer: ConfigSyngas = ConfigSection(commonBuilder, "SyngasProducer", new ConfigSyngas(commonBuilder))
  val HeatExchanger: ConfigExchanger = ConfigSection(commonBuilder, "HeatExchanger", new ConfigExchanger(commonBuilder))
  val Modules: ModulesConfig = ConfigSection(commonBuilder, "Modules", new ModulesConfig(commonBuilder))

  val Integration: IntegrationConfig = ConfigSection(commonBuilder, "Integration", new IntegrationConfig(commonBuilder))

  val COMMON: ForgeConfigSpec = commonBuilder.build()

  def init(): Unit = {
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON)
    CarbonValueRegistry.init()
    TurbineFuelRegistry.init()
    ExchangerRecipeRegistry.init()
  }
}
