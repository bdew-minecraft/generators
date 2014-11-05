/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.config.loader

import net.bdew.lib.recipes.RecipeParser
import net.bdew.lib.recipes.gencfg.GenericConfigParser

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
