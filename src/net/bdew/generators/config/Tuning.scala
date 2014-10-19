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

import net.bdew.generators.Generators
import net.bdew.lib.recipes.gencfg.{ConfigSection, GenericConfigLoader, GenericConfigParser}
import net.bdew.lib.recipes.{RecipeLoader, RecipeParser, RecipesHelper}

object Tuning extends ConfigSection

object TuningLoader {
  val loader = new RecipeLoader with GenericConfigLoader {
    val cfgStore = Tuning
    override def newParser() = new RecipeParser with GenericConfigParser
  }

  def loadDealayed() = loader.processRecipeStatements()

  def loadConfigFiles() {
    if (!Generators.configDir.exists()) {
      Generators.configDir.mkdir()
      val nl = System.getProperty("line.separator")
      val f = new FileWriter(new File(Generators.configDir, "readme.txt"))
      f.write("Any .cfg files in this directory will be loaded after the internal configuration, in alpahabetic order" + nl)
      f.write("Files in 'overrides' directory with matching names cab be used to override internal configuration" + nl)
      f.close()
    }

    RecipesHelper.loadConfigs(
      modName = "Advanced Generators",
      listResource = "/assets/advgenerators/config/files.lst",
      configDir = Generators.configDir,
      resBaseName = "/assets/advgenerators/config/",
      loader = loader)
  }
}




