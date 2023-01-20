package net.bdew.generators.datagen

import net.bdew.generators.registries.Blocks
import net.bdew.lib.datagen.LootTableUtils
import net.minecraft.data.loot.LootTableSubProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.storage.loot.LootTable

import java.util.function.BiConsumer

class BlockLootTablesSubProvider extends LootTableSubProvider {
  override def generate(consumer: BiConsumer[ResourceLocation, LootTable.Builder]): Unit = {
    Blocks.all.foreach(blockReg => {
      val block = blockReg.get()
      LootTableUtils.addBlockEntry(block, LootTableUtils.makeSimpleDropTable(block), consumer)
    })
  }
}
