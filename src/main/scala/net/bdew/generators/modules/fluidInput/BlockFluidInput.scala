package net.bdew.generators.modules.fluidInput

import net.bdew.generators.modules.BaseModule
import net.bdew.generators.registries.Modules
import net.bdew.lib.capabilities.Capabilities
import net.bdew.lib.capabilities.helpers.FluidHelper
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.{BlockPos, BlockRayTraceResult}
import net.minecraft.util.{ActionResultType, Hand, SoundEvents}
import net.minecraft.world.World
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction

class BlockFluidInput extends BaseModule[TileFluidInput](Modules.fluidInput) {
  override def use(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockRayTraceResult): ActionResultType = {
    if (!world.isClientSide) {
      val item = player.getItemInHand(hand)
      if (!item.isEmpty) {
        val didSomething = item.getCapability(Capabilities.CAP_FLUID_HANDLER_ITEM).map[Boolean](cap => {
          val fluid = cap.drain(Int.MaxValue, FluidAction.SIMULATE)
          if (!fluid.isEmpty) {
            val moved = getTE(world, pos).getCapability(Capabilities.CAP_FLUID_HANDLER).map[FluidStack](ourCap => {
              FluidHelper.pushFluid(cap, ourCap)
            }).orElse(FluidStack.EMPTY)
            if (moved.isEmpty) {
              false
            } else {
              world.playSound(null, pos, SoundEvents.BUCKET_EMPTY, player.getSoundSource, 1, 1)
              player.setItemInHand(hand, cap.getContainer)
              true
            }
          } else false
        }).orElse(false)
        if (didSomething) return ActionResultType.CONSUME
      }
    }
    super.use(state, world, pos, player, hand, hit)
  }
}