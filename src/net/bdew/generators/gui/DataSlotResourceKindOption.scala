/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license
 */

package net.bdew.generators.gui

import net.bdew.lib.Misc
import net.bdew.lib.data.DataSlotOption
import net.bdew.lib.data.base.{TileDataSlots, UpdateKind}
import net.bdew.lib.resource.{ResourceKind, ResourceManager}
import net.minecraft.nbt.NBTTagCompound

case class DataSlotResourceKindOption(name: String, parent: TileDataSlots) extends DataSlotOption[ResourceKind] {
  setUpdate(UpdateKind.SAVE, UpdateKind.GUI)

  override def save(t: NBTTagCompound, kind: UpdateKind.Value) {
    value map { r =>
      t.setTag(name,
        Misc.applyMutator(new NBTTagCompound) { tag =>
          r.helperObject.saveToNBT(tag, r)
          tag.setString("kind", r.helperObject.id)
        }
      )
    }
  }

  override def load(t: NBTTagCompound, kind: UpdateKind.Value) {
    if (t.hasKey(name)) {
      val tag = t.getCompoundTag(name)
      value = if (tag.hasKey("kind")) {
        ResourceManager.resourceHelpers.get(tag.getString("kind")).flatMap(_.loadFromNBT(tag))
      } else None
    } else unset()
  }
}
