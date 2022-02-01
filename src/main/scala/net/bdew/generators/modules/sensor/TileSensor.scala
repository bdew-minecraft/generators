package net.bdew.generators.modules.sensor

import net.bdew.generators.registries.{Machines, Modules}
import net.bdew.generators.sensor.Sensors
import net.bdew.lib.Text
import net.bdew.lib.sensors.multiblock.TileRedstoneSensorModule
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.{Inventory, Player}
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class TileSensor(teType: BlockEntityType[_], pos: BlockPos, state: BlockState) extends TileRedstoneSensorModule(Modules.sensor, Sensors, Machines.moduleSensor.block.get(), teType, pos, state) {
  override def getDisplayName: Component = Text.translate("advgenerators.gui.sensor.title")
  override def createMenu(id: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu =
    new ContainerSensor(this, playerInventory, id)
}
