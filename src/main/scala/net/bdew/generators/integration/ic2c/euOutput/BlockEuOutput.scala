package net.bdew.generators.integration.ic2c.euOutput

import net.bdew.generators.integration.ic2c.IC2Tier
import net.bdew.generators.modules.BaseModule
import net.bdew.generators.registries.Modules
import net.bdew.lib.block.WrenchableBlock
import net.bdew.lib.multiblock.block.BlockOutput
import net.bdew.lib.rotate.StatefulBlockFacing
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.{BlockGetter, Level, LevelReader}
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.{InteractionHand, InteractionResult}

class BlockEuOutput(val tier: IC2Tier) extends BaseModule[TileEuOutput](Modules.powerOutput) with StatefulBlockFacing with BlockOutput[TileEuOutput] with WrenchableBlock {
  override def canConnectRedstone(state: BlockState, world: BlockGetter, pos: BlockPos, side: Direction): Boolean = true

  override def onBlockStateChange(level: LevelReader, pos: BlockPos, oldState: BlockState, newState: BlockState): Unit = {
    if (!level.isClientSide && oldState.getBlock == this && newState.getBlock == this && getFacing(oldState) != getFacing(newState))
      getTE(level, pos).foreach(_.facingChanged())
    super.onBlockStateChange(level, pos, oldState, newState)
  }

  override def wrenched(state: BlockState, world: Level, pos: BlockPos, player: Player, hand: InteractionHand, stack: ItemStack, hit: BlockHitResult): InteractionResult = {
    if (!getValidFacings.contains(hit.getDirection)) return InteractionResult.PASS
    if (world.isClientSide) return InteractionResult.SUCCESS
    setFacing(world, pos, hit.getDirection)
    InteractionResult.CONSUME
  }
}