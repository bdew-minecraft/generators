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
import net.bdew.generators.controllers.syngas.TileSyngasController
import net.bdew.generators.controllers.turbine.TileTurbineController
import net.bdew.lib.multiblock.tile.{TileController, TileModule}

object WailaHandler {
  var handlers = Set.empty[ControllerHandlerPair[_]]

  def registerMachineHandler[T <: TileController](teClass: Class[T], handler: BaseControllerDataProvider[T]) = {
    handlers += ControllerHandlerPair(teClass, handler)
  }

  registerMachineHandler(classOf[TileTurbineController], FuelTurbineDataProvider)
  registerMachineHandler(classOf[TileSteamTurbineController], SteamTurbineDataProvider)
  registerMachineHandler(classOf[TileExchangerController], HeatExchangerDataProvider)
  registerMachineHandler(classOf[TileSyngasController], SyngasProducerDataProvider)

  def loadCallback(reg: IWailaRegistrar) {
    Generators.logInfo("WAILA callback received, loading...")

    for (pair <- handlers) {
      reg.registerNBTProvider(pair.handler, pair.teClass)
      reg.registerBodyProvider(pair.handler, pair.teClass)
    }

    reg.registerNBTProvider(ModuleDataProvider, classOf[TileModule])
    reg.registerBodyProvider(ModuleDataProvider, classOf[TileModule])
  }
}
