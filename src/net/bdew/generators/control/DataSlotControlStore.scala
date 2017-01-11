/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.control

import net.bdew.lib.PimpVanilla._
import net.bdew.lib.data.base.{DataSlotContainer, DataSlotVal, UpdateKind}
import net.minecraft.nbt.NBTTagCompound

case class DataSlotControlStore(name: String, parent: DataSlotContainer) extends DataSlotVal[Map[ControlAction, ControlResult.Value]] {
  override def default = Map.empty

  setUpdate(UpdateKind.SAVE)

  def clear() = update(Map.empty)

  override def save(t: NBTTagCompound, kind: UpdateKind.Value): Unit = {
    val tag = new NBTTagCompound
    for ((action, result) <- value) tag.set(action.uid, result.toString)
    t.setTag(name, tag)
  }

  override def loadValue(t: NBTTagCompound, kind: UpdateKind.Value): Map[ControlAction, ControlResult.Value] = {
    import scala.collection.JavaConversions._
    val tag = t.getCompoundTag(name)
    val values = for (key <- tag.getKeySet.toList; action <- ControlActions.registry.get(key); value = ControlResult.withName(tag.getString(key)))
      yield action -> value
    values.toMap
  }
}
