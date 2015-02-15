/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license
 */

package net.bdew.generators.waila

import net.bdew.generators.config.Config
import net.bdew.generators.controllers.steam.TileSteamTurbineController
import net.bdew.lib.{DecFormat, Misc}
import net.minecraft.nbt.NBTTagCompound

object SteamTurbineDataProvider extends BaseControllerDataProvider(classOf[TileSteamTurbineController]) {
  override def getBodyStringsFromData(te: TileSteamTurbineController, data: NBTTagCompound) = {
    loadData(te, data)
    List(
      if (te.steam.getFluid == null) {
        Misc.toLocal("advgenerators.waila.turbine.nosteam")
      } else {
        "%s/%s mB %s".format(DecFormat.round(te.steam.getFluidAmount), DecFormat.round(te.steam.getCapacity), te.steam.getFluid.getLocalizedName)
      },
      "%s/%s %s".format(DecFormat.round(te.power.stored * Config.powerShowMultiplier), DecFormat.round(te.power.capacity * Config.powerShowMultiplier), Config.powerShowUnits),
      DecFormat.short(te.speed) + " RPM",
      Misc.toLocalF("advgenerators.waila.turbine.consuming", DecFormat.short(te.steamAverage.average), "mB"),
      Misc.toLocalF("advgenerators.waila.turbine.producing", DecFormat.short(te.outputAverage.average * Config.powerShowMultiplier), Config.powerShowUnits)
    )
  }
}
