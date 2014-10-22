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
import net.bdew.lib.recipes.{RecipeLoader, RecipeParser, RecipeStatement, RecipesHelper}
import net.minecraftforge.fluids.FluidRegistry

object Tuning extends ConfigSection

object TuningLoader {

  case class RsTurbineFuel(fluid: String, value: Float) extends RecipeStatement

  case class RsTurbineBlacklist(fluid: String) extends RecipeStatement

  class Parser extends RecipeParser with GenericConfigParser {
    def valOrDisable = (
      (decimalNumber <~ "MJ/mB") ^^ { case x => Some(x.toFloat) }
        | "BLACKLIST" ^^^ None
      )

    def turbineFuel = "turbine-fuel" ~> ":" ~> str ~ valOrDisable ^^ {
      case fluid ~ Some(v) => RsTurbineFuel(fluid, v)
      case fluid ~ None => RsTurbineBlacklist(fluid)
    }

    override def recipeStatement = super.recipeStatement | turbineFuel
  }

  val loader = new RecipeLoader with GenericConfigLoader {
    val cfgStore = Tuning

    def getFluid(s: String) =
      Option(FluidRegistry.getFluid(s)).getOrElse(error("Fluid %s not found", s))

    override def newParser() = new Parser
    override def processRecipeStatement(st: RecipeStatement) = st match {

      case RsTurbineFuel(fluid, value) =>
        Generators.logInfo("Adding turbine fuel %s at %f MJ/mB", fluid, value)
        TurbineFuel.addFuel(getFluid(fluid), value)

      case RsTurbineBlacklist(fluid) =>
        Generators.logInfo("Blacklisting turbine fuel %s")
        TurbineFuel.removeFuel(getFluid(fluid))

      case _ => super.processRecipeStatement(st)
    }
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




