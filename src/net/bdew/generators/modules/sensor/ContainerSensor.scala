/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.sensor

import net.bdew.lib.Misc
import net.bdew.lib.data.base.ContainerDataSlots
import net.bdew.lib.gui.{NoInvContainer, SlotClickable}
import net.bdew.lib.sensors.SensorPair
import net.bdew.lib.tile.inventory.BaseInventory
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Slot

class ContainerSensor(val te: TileSensor, player: EntityPlayer) extends NoInvContainer with ContainerDataSlots {
  lazy val dataSource = te

  val fakeInv = new BaseInventory {
    override def getSizeInventory = 2
    override def markDirty() {}
  }

  addSlotToContainer(new Slot(fakeInv, 0, 53, 38) with SlotClickable {
    override def onClick(button: Int, mods: Int, player: EntityPlayer) = {
      te.getCore map { core =>
        val newSensor = Misc.nextInSeq(core.sensorTypes, te.config.sensor)
        te.config := SensorPair(newSensor, newSensor.defaultParameter)
      }
      player.inventory.getItemStack
    }
  })

  addSlotToContainer(new Slot(fakeInv, 1, 71, 38) with SlotClickable {
    override def onClick(button: Int, mods: Int, player: EntityPlayer) = {
      val newParam = te.config.sensor.paramClicked(te.config.param, player.inventory.getItemStack, button, mods)
      te.config := te.config.value.copy(param = newParam)
      player.inventory.getItemStack
    }
  })

  bindPlayerInventory(player.inventory, 8, 94, 152)

  override def canInteractWith(player: EntityPlayer) = true
}
