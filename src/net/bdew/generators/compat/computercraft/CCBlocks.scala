/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.compat.computercraft

import dan200.computercraft.api.ComputerCraftAPI
import net.bdew.generators.modules.dataport.{BlockDataPort, TileDataPort}

object CCBlocks {
  def init(): Unit = {
    ComputerCraftAPI.registerPeripheralProvider(new PeripheralProviderSelective[TileDataPort](BlockDataPort.selectors))
  }
}
