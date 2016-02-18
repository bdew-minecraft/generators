/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.config

import net.bdew.generators.controllers.exchanger.MachineExchanger
import net.bdew.generators.controllers.steam.MachineSteamTurbine
import net.bdew.generators.controllers.syngas.MachineSyngas
import net.bdew.generators.controllers.turbine.MachineTurbine
import net.bdew.lib.config.{MachineManager, MachineManagerMultiblock}

object Machines extends MachineManager(Tuning.getSection("Machines"), Config.guiHandler, Blocks) with MachineManagerMultiblock {
  registerMachine(MachineTurbine)
  registerMachine(MachineExchanger)
  registerMachine(MachineSteamTurbine)
  registerMachine(MachineSyngas)
}
