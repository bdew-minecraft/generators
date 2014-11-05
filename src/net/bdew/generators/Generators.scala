/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators

import java.io.File

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.relauncher.Side
import net.bdew.generators.compat.PowerProxy
import net.bdew.generators.config.loader.TuningLoader
import net.bdew.generators.config.{Config, TurbineFuel}
import org.apache.logging.log4j.Logger

@Mod(modid = Generators.modId, version = "GENERATORS_VER", name = "Advanced Generators", dependencies = "after:BuildCraft|energy;after:BuildCraft|Silicon;after:IC2;after:CoFHCore;after:ThermalExpansion;required-after:bdlib", modLanguage = "scala")
object Generators {
  var log: Logger = null
  var instance = this

  final val modId = "advgenerators"

  var configDir: File = null

  def logInfo(msg: String, args: Any*) = log.info(msg.format(args: _*))
  def logWarn(msg: String, args: Any*) = log.warn(msg.format(args: _*))

  @EventHandler
  def preInit(event: FMLPreInitializationEvent) {
    log = event.getModLog
    configDir = new File(event.getModConfigurationDirectory, "AdvGenerators")
    PowerProxy.logModVersions()
    TuningLoader.loadConfigFiles()
    Config.load()
    if (event.getSide == Side.CLIENT) {
      IconCache.init()
    }
  }

  @EventHandler
  def init(event: FMLInitializationEvent) {
    TurbineFuel.init()
    NetworkRegistry.INSTANCE.registerGuiHandler(this, Config.guiHandler)
  }

  @EventHandler
  def postInit(event: FMLPostInitializationEvent) {
    TuningLoader.loadDealayed()
    TurbineFuel.postInit()
  }
}