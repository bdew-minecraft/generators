/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license
 */

package net.bdew.generators.modules.pressure

import net.bdew.lib.multiblock.tile.{TileController, TileModule}
import net.bdew.pressure.api.{IPressureTile, PressureAPI}

trait PressureModule extends TileModule with IPressureTile {
  override def getWorld = getWorldObj
  override def getXCoord = xCoord
  override def getYCoord = yCoord
  override def getZCoord = zCoord

  override def connect(target: TileController) {
    super.connect(target)
    PressureAPI.HELPER.notifyBlockChanged(getWorldObj, xCoord, yCoord, zCoord)
  }

  override def coreRemoved() {
    PressureAPI.HELPER.notifyBlockChanged(getWorldObj, xCoord, yCoord, zCoord)
    super.coreRemoved()
  }
}
