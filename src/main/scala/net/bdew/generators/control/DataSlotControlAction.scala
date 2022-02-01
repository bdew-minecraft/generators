package net.bdew.generators.control

import net.bdew.lib.data.base.{DataSlotContainer, DataSlotVal, UpdateKind}
import net.minecraft.nbt.CompoundTag

case class DataSlotControlAction(name: String, parent: DataSlotContainer) extends DataSlotVal[ControlAction] {
  override def default: ControlAction = ControlActions.disabled

  override def loadValue(t: CompoundTag, kind: UpdateKind.Value): ControlAction =
    ControlActions.registry.getOrElse(t.getString(name), ControlActions.disabled)

  override def save(t: CompoundTag, kind: UpdateKind.Value): Unit = {
    t.putString(name, value.uid)
  }
}
