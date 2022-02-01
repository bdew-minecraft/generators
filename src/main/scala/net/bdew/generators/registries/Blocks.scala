package net.bdew.generators.registries

import net.bdew.lib.managers.BlockManager
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.minecraft.world.level.material.Material

object Blocks extends BlockManager(Items) {
  def machineProps: Properties = props(Material.STONE)
    .sound(SoundType.STONE)
    .strength(2, 8)
}
