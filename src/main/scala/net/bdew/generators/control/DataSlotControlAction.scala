package net.bdew.generators.control

import net.bdew.lib.data.base.{DataSlotContainer, DataSlotVal, UpdateKind}
import net.minecraft.nbt.CompoundNBT

case class DataSlotControlAction(name: String, parent: DataSlotContainer) extends DataSlotVal[ControlAction] {
  override def default: ControlAction = ControlActions.disabled

  override def loadValue(t: CompoundNBT, kind: UpdateKind.Value): ControlAction =
    ControlActions.registry.getOrElse(t.getString(name), ControlActions.disabled)

  override def save(t: CompoundNBT, kind: UpdateKind.Value): Unit = {
    t.putString(name, value.uid)
  }
}
