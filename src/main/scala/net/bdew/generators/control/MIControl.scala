package net.bdew.generators.control

import net.bdew.lib.multiblock.tile.TileModule

trait MIControl extends TileModule {
  def getControlState(action: ControlAction): ControlResult.Value
}
