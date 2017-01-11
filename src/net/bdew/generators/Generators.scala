/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators

import java.io.File

import net.bdew.generators.compat.PowerProxy
import net.bdew.generators.config._
import net.bdew.generators.config.loader.TuningLoader
import net.bdew.generators.network.NetworkHandler
import net.bdew.generators.sensor.Sensors
import net.bdew.lib.multiblock.data.{OutputConfigItems, OutputConfigManager}
import net.minecraft.item.Item
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event._
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import org.apache.logging.log4j.Logger

@Mod(modid = Generators.modId, version = "GENERATORS_VER", name = "Advanced Generators", dependencies = "after:pressure;after:BuildCraft|energy;after:BuildCraft|Silicon;after:IC2;after:CoFHCore;after:ThermalExpansion;after:eng_toolbox;after:minechem;required-after:bdlib", modLanguage = "scala")
object Generators {
  var log: Logger = null
  var instance = this

  final val modId = "advgenerators"
  final val channel = "bdew.generators"

  var configDir: File = null

  def logDebug(msg: String, args: Any*) = log.debug(msg.format(args: _*))
  def logInfo(msg: String, args: Any*) = log.info(msg.format(args: _*))
  def logWarn(msg: String, args: Any*) = log.warn(msg.format(args: _*))
  def logError(msg: String, args: Any*) = log.error(msg.format(args: _*))
  def logWarnException(msg: String, t: Throwable, args: Any*) = log.warn(msg.format(args: _*), t)
  def logErrorException(msg: String, t: Throwable, args: Any*) = log.error(msg.format(args: _*), t)

  @EventHandler
  def preInit(event: FMLPreInitializationEvent) {
    log = event.getModLog
    configDir = new File(event.getModConfigurationDirectory, "AdvGenerators")
    PowerProxy.logModVersions()
    TuningLoader.loadConfigFiles()
    OutputConfigManager.register("items", () => new OutputConfigItems)

    Items.load()
    Blocks.load()
    Machines.load()

    if (event.getSide == Side.CLIENT) {
      GeneratorsClient.preInit()
    }
  }

  @EventHandler
  def init(event: FMLInitializationEvent) {
    TurbineFuel.init()
    Sensors.init()
    NetworkRegistry.INSTANCE.registerGuiHandler(this, Config.guiHandler)
    NetworkHandler.init()
    //    FMLInterModComms.sendMessage("Waila", "register", "net.bdew.generators.waila.WailaHandler.loadCallback")
    TurbineMaterials.init()
    CapacitorMaterials.init()
    TuningLoader.loadDelayed()
    //    if (Misc.haveModVersion("OpenComputers"))
    //      OCBlocks.init()
    //    if (Misc.haveModVersion("ComputerCraft"))
    //      CCBlocks.init()
  }

  @EventHandler
  def imcCallback(event: FMLInterModComms.IMCEvent) {
    import scala.collection.JavaConversions._
    for (msg <- event.getMessages) {
      IMC.processMessage(msg)
    }
  }

  @EventHandler
  def postInit(event: FMLPostInitializationEvent) {
    TurbineFuel.postInit()
  }

  @EventHandler
  def missingMappings(event: FMLMissingMappingsEvent): Unit = {
    import scala.collection.JavaConversions._
    for (missing <- event.getAll) {
      (missing.name, missing.`type`) match {
        case ("advgenerators:Turbine", GameRegistry.Type.BLOCK) =>
          missing.remap(TurbineMaterials.registry("Iron").turbineBlock.get)
        case ("advgenerators:Turbine", GameRegistry.Type.ITEM) =>
          missing.remap(Item.getItemFromBlock(TurbineMaterials.registry("Iron").turbineBlock.get))
        case ("advgenerators:TurbineRotor", GameRegistry.Type.ITEM) =>
          missing.remap(TurbineMaterials.registry("Iron").rotorItem.get)
        case ("advgenerators:TurbineBlade", GameRegistry.Type.ITEM) =>
          missing.remap(TurbineMaterials.registry("Iron").bladeItem.get)
        case ("advgenerators:PowerCapacitor", GameRegistry.Type.BLOCK) =>
          missing.remap(CapacitorMaterials.registry("Redstone").capacitorBlock.get)
        case ("advgenerators:PowerCapacitor", GameRegistry.Type.ITEM) =>
          missing.remap(Item.getItemFromBlock(CapacitorMaterials.registry("Redstone").capacitorBlock.get))
        case _ => // do nothing
      }
    }
  }

}