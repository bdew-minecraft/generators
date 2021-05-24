package net.bdew.generators.modules.turbine

import net.bdew.generators.modules.BaseModule
import net.bdew.generators.registries.Modules

class BlockTurbine(val cfg: ConfigTurbine) extends BaseModule[TileTurbine](Modules.turbine)