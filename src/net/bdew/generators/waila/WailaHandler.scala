/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license
 */

package net.bdew.generators.waila

import mcp.mobius.waila.api.IWailaRegistrar
import net.bdew.generators.Generators
import net.bdew.generators.controllers.exchanger.TileExchangerController
import net.bdew.generators.controllers.steam.TileSteamTurbineController
import net.bdew.generators.controllers.turbine.TileTurbineController
import net.bdew.lib.multiblock.tile.TileModule

object WailaHandler {
  def loadCallback(reg: IWailaRegistrar) {
    Generators.logInfo("WAILA callback received, loading...")

    reg.registerNBTProvider(FuelTurbineDataProvider, classOf[TileTurbineController])
    reg.registerBodyProvider(FuelTurbineDataProvider, classOf[TileTurbineController])

    reg.registerNBTProvider(SteamTurbineDataProvider, classOf[TileSteamTurbineController])
    reg.registerBodyProvider(SteamTurbineDataProvider, classOf[TileSteamTurbineController])

    reg.registerNBTProvider(HeatExchangerDataProvider, classOf[TileExchangerController])
    reg.registerBodyProvider(HeatExchangerDataProvider, classOf[TileExchangerController])

    reg.registerNBTProvider(ModuleDataProvider, classOf[TileModule])
    reg.registerBodyProvider(ModuleDataProvider, classOf[TileModule])
  }
}
