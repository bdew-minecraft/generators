package net.bdew.generators.datagen

import net.minecraftforge.data.event.GatherDataEvent

object DataGeneration {
  def onGatherData(ev: GatherDataEvent): Unit = {
    val dataGenerator = ev.getGenerator
    val efh = ev.getExistingFileHelper
    dataGenerator.addProvider(true, new LootTables(dataGenerator))
    dataGenerator.addProvider(true, new BlockStates(dataGenerator, efh))
  }
}
