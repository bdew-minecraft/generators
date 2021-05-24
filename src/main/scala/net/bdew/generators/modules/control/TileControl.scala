package net.bdew.generators.modules.control

import net.bdew.generators.control._
import net.bdew.generators.registries.Modules
import net.bdew.lib.Text
import net.bdew.lib.data.DataSlotBoolean
import net.bdew.lib.data.base.{DataSlot, UpdateKind}
import net.bdew.lib.multiblock.ModuleType
import net.bdew.lib.multiblock.tile.TileModule
import net.bdew.lib.tile.TileExtended
import net.minecraft.entity.player.{PlayerEntity, PlayerInventory}
import net.minecraft.inventory.container.{Container, INamedContainerProvider}
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.text.ITextComponent

class TileControl(teType: TileEntityType[_]) extends TileExtended(teType) with TileModule with MIControl with INamedContainerProvider {
  override val kind: ModuleType = Modules.control
  override def getCore: Option[CIControl] = getCoreAs[CIControl]

  val action: DataSlotControlAction = DataSlotControlAction("action", this).setUpdate(UpdateKind.SAVE, UpdateKind.GUI)
  val mode: DataSlotBoolean = DataSlotBoolean("mode", this).setUpdate(UpdateKind.SAVE, UpdateKind.GUI)

  def notifyChange(): Unit = {
    getCore.foreach(_.onControlStateChanged())
  }

  override def dataSlotChanged(slot: DataSlot): Unit = {
    if (slot == action || slot == mode) {
      notifyChange()
    }
    super.dataSlotChanged(slot)
  }

  override def getDisplayName: ITextComponent = Text.translate("advgenerators.gui.control.title")
  override def createMenu(id: Int, playerInventory: PlayerInventory, player: PlayerEntity): Container =
    new ContainerControl(this, playerInventory, id)

  override def getControlState(a: ControlAction): ControlResult.Value =
    if (action :== a)
      ControlResult.fromBool(mode :== getLevel.hasNeighborSignal(getBlockPos))
    else
      ControlResult.NEUTRAL
}
