/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license
 */

package net.bdew.generators.gui

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, DataInputStream, DataOutputStream}

import net.bdew.lib.data.base.{DataSlot, TileDataSlots, UpdateKind}
import net.minecraft.nbt.NBTTagCompound

import scala.collection.mutable

case class DataSlotMovingAverage(name: String, parent: TileDataSlots, size: Int) extends DataSlot {
  val values = mutable.Queue.empty[Double]
  var average: Double = 0

  def update(v: Double) = {
    values += v
    if (values.length > size)
      values.dequeue()
    average = values.sum / values.length
    parent.dataSlotChanged(this)
  }

  setUpdate(UpdateKind.GUI, UpdateKind.SAVE)

  override def save(t: NBTTagCompound, kind: UpdateKind.Value) {
    if (kind == UpdateKind.GUI) {
      t.setDouble(name, average)
    } else {
      val b = new ByteArrayOutputStream()
      val o = new DataOutputStream(b)
      o.writeByte(values.size)
      values.foreach(o.writeDouble)
      t.setByteArray(name, b.toByteArray)
      o.close()
    }
  }

  override def load(t: NBTTagCompound, kind: UpdateKind.Value) {
    if (kind == UpdateKind.GUI) {
      average = t.getDouble(name)
    } else {
      values.clear()
      if (t.hasKey(name)) {
        val i = new DataInputStream(new ByteArrayInputStream(t.getByteArray(name)))
        val len = i.readByte()
        values ++= (for (x <- 0 until Math.min(len, size)) yield i.readDouble())
        i.close()
      }
    }
  }
}
