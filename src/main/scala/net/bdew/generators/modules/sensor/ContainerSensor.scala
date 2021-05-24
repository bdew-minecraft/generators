package net.bdew.generators.modules.sensor

import net.bdew.generators.registries.Containers
import net.bdew.generators.sensor.Sensors
import net.bdew.lib.container.NoInvContainer
import net.bdew.lib.data.base.{ContainerDataSlots, TileDataSlots}
import net.bdew.lib.inventory.SimpleInventory
import net.bdew.lib.sensors._
import net.minecraft.entity.player.PlayerInventory

class ContainerSensor(val te: TileSensor, playerInventory: PlayerInventory, id: Int)
  extends NoInvContainer(Containers.sensor.get(), id) with ContainerDataSlots {

  lazy val dataSource: TileDataSlots = te

  val fakeInv = new SimpleInventory(2)

  addSlot(new SlotSensorType(fakeInv, 0, 53, 38, te.config,
    te.getCore.map(_.redstoneSensorsType).getOrElse(List(Sensors.DisabledSensor))))

  addSlot(new SlotSensorParameter(fakeInv, 1, 71, 38, te.config, te.getCore))

  bindPlayerInventory(playerInventory, 8, 94, 152)
}
