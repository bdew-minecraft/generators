package net.bdew.generators.control

import net.bdew.lib.PimpVanilla._
import net.bdew.lib.data.base.{DataSlotContainer, DataSlotVal, UpdateKind}
import net.bdew.lib.nbt.NBT
import net.minecraft.nbt.CompoundTag

import scala.jdk.CollectionConverters._

case class DataSlotControlStore(name: String, parent: DataSlotContainer) extends DataSlotVal[Map[ControlAction, ControlResult.Value]] {
  override def default: Map[ControlAction, ControlResult.Value] = Map.empty

  setUpdate(UpdateKind.SAVE)

  def clear(): Unit = update(Map.empty)

  override def save(t: CompoundTag, kind: UpdateKind.Value): Unit = {
    t.put(name, NBT.from({ tag =>
      for ((action, result) <- value) tag.setVal(action.uid, result.toString)
    }))
  }

  override def loadValue(t: CompoundTag, kind: UpdateKind.Value): Map[ControlAction, ControlResult.Value] = {
    val tag = t.getCompound(name)
    val values = for (key <- tag.getAllKeys.asScala; action <- ControlActions.registry.get(key); value = ControlResult.withName(tag.getString(key)))
      yield action -> value
    values.toMap
  }
}
