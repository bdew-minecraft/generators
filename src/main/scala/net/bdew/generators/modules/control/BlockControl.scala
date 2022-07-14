package net.bdew.generators.modules.control

import net.bdew.generators.modules.BaseModule
import net.bdew.generators.registries.Modules
import net.bdew.lib.multiblock.tile.TileController
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.{BlockState, StateDefinition}
import net.minecraft.world.level.{BlockGetter, Level}
import net.minecraftforge.network.NetworkHooks

class BlockControl extends BaseModule[TileControl](Modules.control) {
  override def getDefaultState(base: BlockState): BlockState =
    super.getDefaultState(base).setValue(BlockStateProperties.POWERED, Boolean.box(false))

  override def createBlockStateDefinition(builder: StateDefinition.Builder[Block, BlockState]): Unit = {
    super.createBlockStateDefinition(builder)
    builder.add(BlockStateProperties.POWERED)
  }

  override def activateGui(state: BlockState, world: Level, pos: BlockPos, controller: TileController, player: Player): Boolean = {
    player match {
      case serverPlayer: ServerPlayer =>
        NetworkHooks.openScreen(serverPlayer, getTE(world, pos), pos)
        true
      case _ => false
    }
  }

  override def canConnectRedstone(state: BlockState, world: BlockGetter, pos: BlockPos, side: Direction): Boolean = true

  override def neighborChanged(state: BlockState, world: Level, pos: BlockPos, block: Block, fromPos: BlockPos, moving: Boolean): Unit = {
    super.neighborChanged(state, world, pos, block, fromPos, moving)
    val wasPowered = state.getValue(BlockStateProperties.POWERED)
    val powered = world.hasNeighborSignal(pos)
    if (powered ^ wasPowered)
      world.setBlock(pos, state.setValue(BlockStateProperties.POWERED, Boolean.box(powered)), 3)
    if (!world.isClientSide) getTE(world, pos).notifyChange()
  }

  override def setPlacedBy(world: Level, pos: BlockPos, state: BlockState, placer: LivingEntity, stack: ItemStack): Unit = {
    super.setPlacedBy(world, pos, state, placer, stack)
    neighborChanged(state, world, pos, this, pos, false)
  }
}
