package net.bdew.generators.modules.control

import net.bdew.generators.modules.BaseModule
import net.bdew.generators.registries.Modules
import net.bdew.lib.multiblock.tile.TileController
import net.minecraft.block.{Block, BlockState}
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.{PlayerEntity, ServerPlayerEntity}
import net.minecraft.item.ItemStack
import net.minecraft.state.StateContainer
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.{IBlockReader, World}
import net.minecraftforge.fml.network.NetworkHooks

class BlockControl extends BaseModule[TileControl](Modules.control) {
  override def getDefaultState(base: BlockState): BlockState =
    super.getDefaultState(base).setValue(BlockStateProperties.POWERED, Boolean.box(false))

  override def createBlockStateDefinition(builder: StateContainer.Builder[Block, BlockState]): Unit = {
    super.createBlockStateDefinition(builder)
    builder.add(BlockStateProperties.POWERED)
  }

  override def activateGui(state: BlockState, world: World, pos: BlockPos, controller: TileController, player: PlayerEntity): Boolean = {
    player match {
      case serverPlayer: ServerPlayerEntity =>
        NetworkHooks.openGui(serverPlayer, getTE(world, pos), pos)
        true
      case _ => false
    }
  }

  override def canConnectRedstone(state: BlockState, world: IBlockReader, pos: BlockPos, side: Direction): Boolean = true

  override def neighborChanged(state: BlockState, world: World, pos: BlockPos, block: Block, fromPos: BlockPos, moving: Boolean): Unit = {
    super.neighborChanged(state, world, pos, block, fromPos, moving)
    val wasPowered = state.getValue(BlockStateProperties.POWERED)
    val powered = world.hasNeighborSignal(pos)
    if (powered ^ wasPowered)
      world.setBlock(pos, state.setValue(BlockStateProperties.POWERED, Boolean.box(powered)), 3)
    if (!world.isClientSide) getTE(world, pos).notifyChange()
  }

  override def setPlacedBy(world: World, pos: BlockPos, state: BlockState, placer: LivingEntity, stack: ItemStack): Unit = {
    super.setPlacedBy(world, pos, state, placer, stack)
    neighborChanged(state, world, pos, this, pos, false)
  }
}
