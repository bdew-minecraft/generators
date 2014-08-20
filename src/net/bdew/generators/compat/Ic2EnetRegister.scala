/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/generators/master/MMPL-1.0.txt
 */

package net.bdew.generators.compat

import ic2.api.energy.event.{EnergyTileLoadEvent, EnergyTileUnloadEvent}
import ic2.api.energy.tile.IEnergyTile
import net.bdew.lib.tile.TileExtended
import net.minecraftforge.common.MinecraftForge

trait Ic2EnetRegister extends TileExtended with IEnergyTile {
  var sentLoaded = false

  if (PowerProxy.haveIC2 && PowerProxy.EUEnabled) {
    serverTick.listen(() => {
      if (!sentLoaded) {
        MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this))
        sentLoaded = true
      }
    })
  }

  override def invalidate() {
    sendUnload()
    super.invalidate()
  }

  override def onChunkUnload() = {
    sendUnload()
    super.onChunkUnload()
  }

  def sendUnload() {
    if (PowerProxy.haveIC2 && sentLoaded) {
      MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this))
      sentLoaded = false
    }
  }
}
