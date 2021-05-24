package net.bdew.generators.modules.sensor

import net.bdew.generators.modules.BaseModule
import net.bdew.generators.registries.Modules
import net.bdew.lib.sensors.multiblock.BlockRedstoneSensorModule


class BlockSensor extends BaseModule[TileSensor](Modules.sensor) with BlockRedstoneSensorModule[TileSensor] {

}
