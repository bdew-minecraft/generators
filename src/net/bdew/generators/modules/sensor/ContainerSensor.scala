/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.sensor

import net.bdew.generators.sensor.Sensors
import net.bdew.lib.data.base.ContainerDataSlots
import net.bdew.lib.gui.NoInvContainer
import net.bdew.lib.sensors._
import net.bdew.lib.tile.inventory.SimpleInventory
import net.minecraft.entity.player.EntityPlayer

class ContainerSensor(val te: TileSensor, player: EntityPlayer) extends NoInvContainer with ContainerDataSlots {
  lazy val dataSource = te

  val fakeInv = new SimpleInventory(2)

  addSlotToContainer(new SlotSensorType(fakeInv, 0, 53, 38, te.config,
    te.getCore.map(_.sensorTypes).getOrElse(List(Sensors.DisabledSensor))))

  addSlotToContainer(new SlotSensorParameter(fakeInv, 1, 71, 38, te.config, te.getCore))

  bindPlayerInventory(player.inventory, 8, 94, 152)

  override def canInteractWith(player: EntityPlayer) = true
}
