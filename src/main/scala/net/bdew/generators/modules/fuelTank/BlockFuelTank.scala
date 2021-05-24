package net.bdew.generators.modules.fuelTank

import net.bdew.generators.modules.BaseModule
import net.bdew.generators.registries.Modules

class BlockFuelTank(val cfg: ConfigFuelTank) extends BaseModule[TileFuelTank](Modules.fuelTank)