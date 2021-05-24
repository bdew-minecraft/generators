package net.bdew.generators.blocks

import net.bdew.lib.render.connected.ConnectedTextureBlock
import net.minecraft.block.AbstractBlock.Properties
import net.minecraft.block.material.Material
import net.minecraft.block.{Block, SoundType}
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockReader

class TestConnectedBlock extends Block(Properties.of(Material.STONE).sound(SoundType.STONE).strength(2, 8))
  with ConnectedTextureBlock {
  override def canConnect(world: IBlockReader, origin: BlockPos, target: BlockPos): Boolean =
    world.getBlockState(target).is(this)
}
