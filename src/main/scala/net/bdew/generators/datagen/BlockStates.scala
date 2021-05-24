package net.bdew.generators.datagen

import net.bdew.generators.Generators
import net.bdew.generators.modules.control.BlockControl
import net.bdew.generators.registries.Blocks
import net.bdew.lib.datagen.BlockStateGenerator
import net.bdew.lib.sensors.multiblock.BlockRedstoneSensorModule
import net.minecraft.data.DataGenerator
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.util.Direction
import net.minecraftforge.client.model.generators.ConfiguredModel
import net.minecraftforge.common.data.ExistingFileHelper

class BlockStates(gen: DataGenerator, efh: ExistingFileHelper) extends BlockStateGenerator(gen, Generators.ModId, efh) {
  override def registerStatesAndModels(): Unit = {
    Blocks.all.foreach(_.get() match {
      case block: BlockControl =>
        val modelOff = uncheckedModel("block/" + block.getRegistryName.getPath + "_off")
        val modelOn = uncheckedModel("block/" + block.getRegistryName.getPath + "_on")
        getVariantBuilder(block)
          .forAllStates(state => {
            val powered = state.getValue(BlockStateProperties.POWERED)
            ConfiguredModel.builder()
              .modelFile(if (powered) modelOn else modelOff)
              .build()
          })
        makeBlockItem(block, modelOff)
      case block: BlockRedstoneSensorModule[_] =>
        val modelOff = uncheckedModel("block/" + block.getRegistryName.getPath + "_off")
        val modelOn = uncheckedModel("block/" + block.getRegistryName.getPath + "_on")
        getVariantBuilder(block)
          .forAllStates(state => {
            val facing = state.getValue(BlockStateProperties.FACING)
            val powered = state.getValue(BlockStateProperties.POWERED)
            val builder = ConfiguredModel.builder()
              .modelFile(if (powered) modelOn else modelOff)
            facing match {
              case Direction.UP => builder.rotationX(270)
              case Direction.DOWN => builder.rotationX(90)
              case Direction.SOUTH => builder.rotationY(180)
              case Direction.WEST => builder.rotationY(270)
              case Direction.EAST => builder.rotationY(90)
              case Direction.NORTH => //nothing
            }
            builder.build()
          })
        makeBlockItem(block, modelOff)
      case x => makeBlock(x)
    })
  }
}
