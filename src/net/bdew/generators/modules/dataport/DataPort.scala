/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.dataport

import net.bdew.generators.control._
import net.bdew.generators.controllers.exchanger.TileExchangerController
import net.bdew.generators.controllers.steam.TileSteamTurbineController
import net.bdew.generators.controllers.syngas.TileSyngasController
import net.bdew.generators.controllers.turbine.TileTurbineController
import net.bdew.generators.modules.BaseModule
import net.bdew.lib.computers.ModuleSelector
import net.bdew.lib.multiblock.tile.{TileController, TileModule}

object BlockDataPort extends BaseModule("generators_data_port", "GeneratorsDataPort", classOf[TileDataPort]) {
  val selectors = List(
    ModuleSelector("ag_gas_turbine", classOf[TileTurbineController], GasTurbineCommands),
    ModuleSelector("ag_steam_turbine", classOf[TileSteamTurbineController], SteamTurbineCommands),
    ModuleSelector("ag_heat_exchanger", classOf[TileExchangerController], HeatExchangerCommands),
    ModuleSelector("ag_syngas_producer", classOf[TileSyngasController], SyngasProducerCommands)
  )
}

class TileDataPort extends TileModule with MIControl {
  val controls = DataSlotControlStore("controls", this)

  override def getControlState(action: ControlAction): ControlResult.Value = controls.getOrElse(action, ControlResult.NEUTRAL)

  override def connect(target: TileController): Unit = {
    super.connect(target)
    if (target.isInstanceOf[CIControl])
      controls := target.asInstanceOf[CIControl].availableControlActions.filterNot(ControlActions.disabled.eq).map(_ -> ControlResult.NEUTRAL).toMap
    else
      controls.clear()
  }

  override def coreRemoved(): Unit = {
    controls.clear()
    super.coreRemoved()
  }

  def setControl(control: ControlAction, result: ControlResult.Value): Unit = {
    controls := controls.value + (control -> result)
    getCoreAs[CIControl].foreach(_.onControlStateChanged())
  }

  val kind: String = "GeneratorsDataPort"
}