/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.control

import net.bdew.lib.data.base.{DataSlotContainer, DataSlotVal, UpdateKind}
import net.minecraft.nbt.NBTTagCompound

case class DataSlotControlAction(name: String, parent: DataSlotContainer) extends DataSlotVal[ControlAction] {
  override def default: ControlAction = ControlActions.disabled

  override def loadValue(t: NBTTagCompound, kind: UpdateKind.Value): ControlAction =
    ControlActions.registry.getOrElse(t.getString(name), ControlActions.disabled)

  override def save(t: NBTTagCompound, kind: UpdateKind.Value): Unit = {
    t.setString(name, value.uid)
  }
}
