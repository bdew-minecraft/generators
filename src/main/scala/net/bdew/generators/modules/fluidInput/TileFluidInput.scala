package net.bdew.generators.modules.fluidInput

import net.bdew.generators.registries.Modules
import net.bdew.lib.capabilities.Capabilities
import net.bdew.lib.capabilities.helpers.fluid.FluidHandlerOptional
import net.bdew.lib.multiblock.ModuleType
import net.bdew.lib.multiblock.interact.CIFluidInput
import net.bdew.lib.multiblock.tile.TileModule
import net.bdew.lib.tile.TileExtended
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.fluids.capability.IFluidHandler

class TileFluidInput(teType: BlockEntityType[_], pos: BlockPos, state: BlockState) extends TileExtended(teType, pos, state) with TileModule {
  val kind: ModuleType = Modules.fluidInput

  override def getCore: Option[CIFluidInput] = getCoreAs[CIFluidInput]

  val handler: LazyOptional[IFluidHandler] = FluidHandlerOptional.create(() => getCore.map(_.fluidInput))

  //noinspection ComparingUnrelatedTypes
  override def getCapability[T](cap: Capability[T], side: Direction): LazyOptional[T] = {
    if (cap == Capabilities.CAP_FLUID_HANDLER) {
      handler.cast()
    } else super.getCapability(cap, side)
  }

  override def invalidateCaps(): Unit = {
    super.invalidateCaps()
    handler.invalidate()
  }
}
