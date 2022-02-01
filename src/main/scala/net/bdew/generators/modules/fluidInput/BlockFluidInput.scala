package net.bdew.generators.modules.fluidInput

import net.bdew.generators.modules.BaseModule
import net.bdew.generators.registries.Modules
import net.bdew.lib.capabilities.Capabilities
import net.bdew.lib.capabilities.helpers.FluidHelper
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.{InteractionHand, InteractionResult}
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction

class BlockFluidInput extends BaseModule[TileFluidInput](Modules.fluidInput) {
  override def use(state: BlockState, world: Level, pos: BlockPos, player: Player, hand: InteractionHand, hit: BlockHitResult): InteractionResult = {
    if (!world.isClientSide) {
      val item = player.getItemInHand(hand)
      if (!item.isEmpty) {
        val didSomething = item.copy().getCapability(Capabilities.CAP_FLUID_HANDLER_ITEM).map[Boolean](cap => {
          val fluid = cap.drain(Int.MaxValue, FluidAction.SIMULATE)
          if (!fluid.isEmpty) {
            val moved = getTE(world, pos).getCapability(Capabilities.CAP_FLUID_HANDLER).map[FluidStack](ourCap => {
              FluidHelper.pushFluid(cap, ourCap)
            }).orElse(FluidStack.EMPTY)
            if (moved.isEmpty) {
              false
            } else {
              world.playSound(null, pos, SoundEvents.BUCKET_EMPTY, player.getSoundSource, 1, 1)
              if (!player.isCreative)
                player.setItemInHand(hand, cap.getContainer)
              true
            }
          } else false
        }).orElse(false)
        if (didSomething) return InteractionResult.CONSUME
      }
    }
    super.use(state, world, pos, player, hand, hit)
  }
}