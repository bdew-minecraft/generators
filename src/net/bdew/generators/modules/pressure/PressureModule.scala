/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.pressure

import net.bdew.lib.multiblock.tile.{TileController, TileModule}
import net.bdew.pressure.api.{IPressureNode, PressureAPI}

trait PressureModule extends TileModule with IPressureNode {
  override def connect(target: TileController) {
    super.connect(target)
    PressureAPI.HELPER.notifyBlockChanged(getWorld, getPos)
  }

  override def coreRemoved() {
    PressureAPI.HELPER.notifyBlockChanged(getWorld, getPos)
    super.coreRemoved()
  }

  override def pressureNodePos = getPos
  override def pressureNodeWorld = getWorld
}
