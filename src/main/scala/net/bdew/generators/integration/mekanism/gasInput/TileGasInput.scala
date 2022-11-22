package net.bdew.generators.integration.mekanism.gasInput

import mekanism.api.chemical.gas.IGasHandler
import net.bdew.generators.integration.mekanism.Caps
import net.bdew.generators.registries.Modules
import net.bdew.lib.multiblock.ModuleType
import net.bdew.lib.multiblock.interact.CIFluidInput
import net.bdew.lib.multiblock.tile.TileModule
import net.bdew.lib.tile.TileExtended
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional

class TileGasInput(teType: BlockEntityType[_], pos: BlockPos, state: BlockState) extends TileExtended(teType, pos, state) with TileModule {
  val kind: ModuleType = Modules.fluidInput

  override def getCore: Option[CIFluidInput] = getCoreAs[CIFluidInput]

  val handler: LazyOptional[IGasHandler] = GasHandlerOptional.create(() => getCore.map(_.fluidInput), getLevel)

  //noinspection ComparingUnrelatedTypes
  override def getCapability[T](cap: Capability[T], side: Direction): LazyOptional[T] = {
    if (cap == Caps.GAS_HANDLER_CAPABILITY) {
      handler.cast()
    } else super.getCapability(cap, side)
  }

  override def invalidateCaps(): Unit = {
    super.invalidateCaps()
    handler.invalidate()
  }
}
