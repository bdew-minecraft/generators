package net.bdew.generators.modules.sensor

import net.bdew.generators.registries.{Machines, Modules}
import net.bdew.generators.sensor.Sensors
import net.bdew.lib.Text
import net.bdew.lib.sensors.multiblock.TileRedstoneSensorModule
import net.minecraft.entity.player.{PlayerEntity, PlayerInventory}
import net.minecraft.inventory.container.Container
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.text.ITextComponent

class TileSensor(teType: TileEntityType[_]) extends TileRedstoneSensorModule(Modules.sensor, Sensors, Machines.moduleSensor.block.get(), teType) {
  override def getDisplayName: ITextComponent = Text.translate("advgenerators.gui.sensor.title")
  override def createMenu(id: Int, playerInventory: PlayerInventory, player: PlayerEntity): Container =
    new ContainerSensor(this, playerInventory, id)
}
