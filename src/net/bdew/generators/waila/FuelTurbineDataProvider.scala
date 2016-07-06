/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.waila

import net.bdew.generators.config.Config
import net.bdew.generators.controllers.turbine.TileTurbineController
import net.bdew.lib.{DecFormat, Misc}

object FuelTurbineDataProvider extends BaseControllerDataProvider(classOf[TileTurbineController]) {
  override def getBodyStringsFromTE(te: TileTurbineController) = {
    List(
      if (te.fuel.getFluid == null) {
        Misc.toLocal("advgenerators.waila.turbine.nofuel")
      } else {
        "%s/%s mB %s".format(DecFormat.round(te.fuel.getFluidAmount), DecFormat.round(te.fuel.getCapacity), te.fuel.getFluid.getLocalizedName)
      },
      "%s/%s %s".format(DecFormat.round(te.power.stored * Config.powerShowMultiplier), DecFormat.round(te.power.capacity * Config.powerShowMultiplier), Config.powerShowUnits),
      Misc.toLocalF("advgenerators.waila.turbine.consuming", DecFormat.short(te.fuelPerTickAverage.average), "mB"),
      Misc.toLocalF("advgenerators.waila.turbine.producing", DecFormat.short(te.outputAverage.average * Config.powerShowMultiplier), Config.powerShowUnits)
    )
  }
}
