package net.bdew.generators.modules.control

import net.bdew.generators.control._
import net.bdew.generators.registries.Modules
import net.bdew.lib.Text
import net.bdew.lib.data.DataSlotBoolean
import net.bdew.lib.data.base.{DataSlot, UpdateKind}
import net.bdew.lib.multiblock.ModuleType
import net.bdew.lib.multiblock.tile.TileModule
import net.bdew.lib.tile.TileExtended
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.{Inventory, Player}
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class TileControl(teType: BlockEntityType[_], pos: BlockPos, state: BlockState) extends TileExtended(teType, pos, state) with TileModule with MIControl with MenuProvider {
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

  override def getDisplayName: Component = Text.translate("advgenerators.gui.control.title")
  override def createMenu(id: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu =
    new ContainerControl(this, playerInventory, id)

  override def getControlState(a: ControlAction): ControlResult.Value =
    if (action :== a)
      ControlResult.fromBool(mode :== getLevel.hasNeighborSignal(getBlockPos))
    else
      ControlResult.NEUTRAL
}
