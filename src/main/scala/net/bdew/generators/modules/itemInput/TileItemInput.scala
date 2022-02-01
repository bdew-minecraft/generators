package net.bdew.generators.modules.itemInput

import net.bdew.generators.registries.Modules
import net.bdew.lib.capabilities.Capabilities
import net.bdew.lib.capabilities.helpers.items.ItemHandlerOptional
import net.bdew.lib.multiblock.ModuleType
import net.bdew.lib.multiblock.interact.CIItemInput
import net.bdew.lib.multiblock.tile.TileModule
import net.bdew.lib.tile.TileExtended
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.IItemHandler

class TileItemInput(teType: BlockEntityType[_], pos: BlockPos, state: BlockState) extends TileExtended(teType, pos, state) with TileModule {
  val kind: ModuleType = Modules.itemInput

  override def getCore: Option[CIItemInput] = getCoreAs[CIItemInput]

  val handler: LazyOptional[IItemHandler] = ItemHandlerOptional.create(() => getCore.map(_.itemInput))

  //noinspection ComparingUnrelatedTypes
  override def getCapability[T](cap: Capability[T], side: Direction): LazyOptional[T] = {
    if (cap == Capabilities.CAP_ITEM_HANDLER) {
      handler.cast()
    } else super.getCapability(cap, side)
  }

  override def invalidateCaps(): Unit = {
    super.invalidateCaps()
    handler.invalidate()
  }
}
