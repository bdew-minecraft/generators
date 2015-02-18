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

  def fluidAmt = str ~ decimalNumber <~ "mB" ^^ { case f ~ n => (f, n.toDouble) }
  def heatUnits = decimalNumber <~ "HU" ^^ (_.toDouble)

  def resRefFluid = "fluid:" ~> str ^^ ResKindFluid
  def resRefItem = spec ^^ ResKindItem

  def resFluid = resRefFluid ~ decimalNumber <~ "mB" ^^ { case f ~ n => ResourceRef(f, n.toDouble) }
  def resItem = resRefItem ~ decimalNumber ^^ { case s ~ n => ResourceRef(s, n.toDouble) }

  def resource = resFluid | resItem
  def resourceRef = resRefFluid | resRefItem

  def exchangerRecipe = "exchanger" ~> ":" ~> (
    resource ~ "+" ~ heatUnits ~ "=>" ~ resource ^^ { case i ~ p ~ hu ~ arw ~ o => RsExchangerCool(i, o, hu) } |
      resource ~ "=>" ~ resource ~ "+" ~ heatUnits ^^ { case i ~ arw ~ o ~ p ~ hu => RsExchangerHeat(i, o, hu) } |
      "BLACKLIST" ~> resourceRef ^^ RsExchangerBlacklist
    )

  def carbonValue =
    ("=>" ~> int ^^ CarbonValueSpecified) |
      "DEFAULT" ^^^ CarbonValueDefault() |
      "BLACKLIST" ^^^ CarbonValueBlacklist()

  def carbonValueRecipe = "carbon-value" ~> ":" ~> spec ~ carbonValue ^^ { case sp ~ cv => RsCarbonValue(sp, cv) }

  override def recipeStatement = super.recipeStatement | turbineFuel | exchangerRecipe | carbonValueRecipe
}
