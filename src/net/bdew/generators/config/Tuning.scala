/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.config

import java.io._

import cpw.mods.fml.common.FMLCommonHandler
import net.bdew.generators.Generators
import net.bdew.lib.recipes.gencfg.{ConfigSection, GenericConfigLoader, GenericConfigParser}
import net.bdew.lib.recipes.{RecipeLoader, RecipeParser}

object Tuning extends ConfigSection

object TuningLoader {
  val loader = new RecipeLoader with GenericConfigLoader {
    val cfgStore = Tuning
    override def newParser() = new RecipeParser with GenericConfigParser
  }

  def loadDealayed() = loader.processDelayedStatements()

  def loadConfigFiles() {
    val listReader = new BufferedReader(new InputStreamReader(
      getClass.getResourceAsStream("/assets/advgenerators/config/files.lst")))
    val list = Iterator.continually(listReader.readLine)
      .takeWhile(_ != null)
      .map(_.trim)
      .filterNot(_.startsWith("#"))
      .filterNot(_.isEmpty)
      .toList
    listReader.close()

    if (!Generators.configDir.exists()) {
      Generators.configDir.mkdir()
      val f = new FileWriter(new File(Generators.configDir, "readme.txt"))
      f.write("Any .cfg files in this directory will be loaded after the internal configuration, in alpahabetic order\n")
      f.write("Files in 'overrides' directory with matching names cab be used to override internal configuration\n")
      f.close()
    }

    val overrideDir = new File(Generators.configDir, "overrides")
    if (!overrideDir.exists()) overrideDir.mkdir()

    Generators.logInfo("Loading internal config files")

    for (fileName <- list) {
      val overrideFile = new File(overrideDir, fileName)
      if (overrideFile.exists()) {
        tryLoadConfig(new FileReader(overrideFile), overrideFile.getCanonicalPath)
      } else {
        val resname = "/assets/advgenerators/config/" + fileName
        tryLoadConfig(new InputStreamReader(getClass.getResourceAsStream(resname)), getClass.getResource(resname).toString)
      }
    }

    Generators.logInfo("Loading user config files")

    for (fileName <- Generators.configDir.list().sorted if fileName.endsWith(".cfg")) {
      val file = new File(Generators.configDir, fileName)
      if (file.canRead) tryLoadConfig(new FileReader(file), file.getCanonicalPath)
    }
  }

  def tryLoadConfig(reader: Reader, path: String) {
    Generators.logInfo("Loading config: %s", path)
    try {
      loader.load(reader)
    } catch {
      case e: Throwable =>
        FMLCommonHandler.instance().raiseException(e, "generators config loading failed in file %s: %s".format(path, e.getMessage), true)
    } finally {
      reader.close()
    }
  }
}




