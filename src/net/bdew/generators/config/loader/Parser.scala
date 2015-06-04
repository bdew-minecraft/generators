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

  def containerRecipe = "set-container" ~> ":" ~> spec ~ "=>" ~ spec ^^ { case i ~ a ~ c => RsSetContainer(i, c) }

  def specCount = spec ~ ("*" ~> int).? ^^ { case s ~ c => StackRefCount(s, c.getOrElse(1)) }

  def enderIoSmelt = "enderio-smelt:" ~> specCount ~ ("+" ~> specCount).* ~ ("+" ~> int <~ "RF") ~ "=>" ~ specCount ~ ("+" ~> decimalNumber <~ "xp").? ^^ {
    case in1 ~ in2 ~ rf ~ arw ~ res ~ xp => RsEnderIOSmelt(List(in1) ++ in2, res, xp.getOrElse("0").toDouble, rf)
  }

  def enderIoSagMill = "enderio-sag-mill:" ~> spec ~ ("+" ~> int <~ "RF") ~ "=>" ~ specCount ~ ("+" ~> specCount).* ~ "NOBONUS".? ^^ {
    case in ~ rf ~ arw ~ out1 ~ out2 ~ nb => RsEnderIOSagMill(in, List(out1) ++ out2, rf, nb.isEmpty)
  }

  def teSmelterRecipe = "TE-smelt:" ~> specCount ~ ("+" ~> specCount).? ~ ("+" ~> int <~ "RF") ~ "=>" ~ specCount ~ ("+" ~> specCount ~ (int <~ "%").?).? ^^ {
    case in1 ~ in2 ~ rf ~ arw ~ out1 ~ None => RsTESmelter(in1, in2, out1, None, rf, 0)
    case in1 ~ in2 ~ rf ~ arw ~ out1 ~ Some(out2 ~ chance) => RsTESmelter(in1, in2, out1, Some(out2), rf, chance.getOrElse(100))
  }

  override def recipeStatement = (super.recipeStatement
    | turbineFuel
    | exchangerRecipe
    | carbonValueRecipe
    | containerRecipe
    | enderIoSmelt
    | enderIoSagMill
    | teSmelterRecipe
    )
}
