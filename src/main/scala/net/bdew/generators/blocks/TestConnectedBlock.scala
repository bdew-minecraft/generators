package net.bdew.generators.blocks

import net.bdew.lib.render.connected.ConnectedTextureBlock
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.minecraft.world.level.block.{Block, SoundType}
import net.minecraft.world.level.material.Material

class TestConnectedBlock extends Block(Properties.of(Material.STONE).sound(SoundType.STONE).strength(2, 8))
  with ConnectedTextureBlock {
  override def canConnect(world: BlockGetter, origin: BlockPos, target: BlockPos): Boolean =
    world.getBlockState(target).is(this)
}
