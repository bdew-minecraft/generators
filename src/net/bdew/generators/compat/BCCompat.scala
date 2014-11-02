/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.compat

import buildcraft.api.fuels.BuildcraftFuelRegistry

object BCCompat {

  import scala.collection.JavaConversions._

  def getCombustionEngineFuels =
    for (fuel <- BuildcraftFuelRegistry.fuel.getFuels) yield {
      fuel.getFluid -> fuel.getPowerPerCycle * fuel.getTotalBurningTime / 1000F
    }
}
