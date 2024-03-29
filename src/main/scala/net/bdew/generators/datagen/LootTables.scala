package net.bdew.generators.datagen

import net.bdew.generators.Generators
import net.bdew.generators.registries.Blocks
import net.bdew.lib.datagen.LootTableGenerator
import net.minecraft.data.DataGenerator
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.storage.loot.LootTable

class LootTables(gen: DataGenerator) extends LootTableGenerator(gen, Generators.ModId) {
  override def makeTables(): Map[ResourceLocation, LootTable] = {
    Blocks.all.map(blockReg => {
      val block = blockReg.get()
      makeBlockEntry(block, makeSimpleDropTable(block))
    }).toMap
  }
}
