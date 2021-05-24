package net.bdew.generators.modules.powerCapacitor

import net.bdew.generators.modules.BaseModule
import net.bdew.generators.registries.Modules

class BlockCapacitor(val cfg: ConfigCapacitor) extends BaseModule[TileCapacitor](Modules.powerCapacitor)