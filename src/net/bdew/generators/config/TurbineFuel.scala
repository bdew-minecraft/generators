/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.config

import net.bdew.generators.Generators
import net.minecraftforge.fluids.{Fluid, FluidStack}

object TurbineFuel {
  var map = Map.empty[String, Float]

  def init() {
    // FIXME: reenable when bc is released
    //    val bcCfg = Tuning.getSection("ModSupport").getSection("BuildCraft")
    //    if (bcCfg.getBoolean("ImportCombustionEngineFuels") && PowerProxy.haveBCFuel) {
    //      val min = bcCfg.getDouble("TurbineMinimumFuelValue")
    //      map ++= BCCompat.getCombustionEngineFuels filter (_._2 >= min)
    //    }
  }

  def postInit() {
    Generators.logInfo("Turbine fuels:")
    for ((fuel, value) <- map)
      Generators.logInfo(" * %s: %.0f MJ/MB".format(fuel, value))
  }

  def isValidFuel(fs: FluidStack): Boolean = fs != null && isValidFuel(fs.getFluid)
  def isValidFuel(f: Fluid): Boolean = f != null && map.contains(f.getName)

  def addFuel(f: Fluid, v: Float) = map += (f.getName -> v)
  def removeFuel(f: Fluid) = map -= f.getName

  def getFuelValue(fluid: Fluid) = map.getOrElse(fluid.getName, 0F)
}
