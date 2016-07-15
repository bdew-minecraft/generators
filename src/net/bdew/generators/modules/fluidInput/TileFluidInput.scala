/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.fluidInput

import net.bdew.lib.capabilities.helpers.FluidMultiHandler
import net.bdew.lib.capabilities.legacy.OldFluidHandlerEmulator
import net.bdew.lib.capabilities.{Capabilities, CapabilityProvider}
import net.bdew.lib.multiblock.interact.CIFluidInput
import net.bdew.lib.multiblock.tile.TileModule

class TileFluidInput extends TileModule with CapabilityProvider with OldFluidHandlerEmulator {
  val kind: String = "FluidInput"

  override def getCore = getCoreAs[CIFluidInput]

  addCapabilityOption(Capabilities.CAP_FLUID_HANDLER) { side =>
    getCore.map(core => FluidMultiHandler.wrap(core.getInputTanks))
  }
}
