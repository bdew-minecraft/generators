package net.bdew.generators.modules.fluidInput

import net.bdew.generators.modules.BaseModule
import net.bdew.generators.registries.Modules
import net.bdew.lib.capabilities.helpers.FluidHelper
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.{InteractionHand, InteractionResult}

class BlockFluidInput extends BaseModule[TileFluidInput](Modules.fluidInput) {
  override def use(state: BlockState, world: Level, pos: BlockPos, player: Player, hand: InteractionHand, hit: BlockHitResult): InteractionResult = {
    if (!world.isClientSide) {
      if (FluidHelper.blockFluidInteract(world, pos, player, hand))
        return InteractionResult.CONSUME
    }
    super.use(state, world, pos, player, hand, hit)
  }
}