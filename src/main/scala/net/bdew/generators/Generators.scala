package net.bdew.generators

import net.bdew.generators.config.Config
import net.bdew.generators.datagen.DataGeneration
import net.bdew.generators.network.NetworkHandler
import net.bdew.generators.registries._
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext

@Mod(Generators.ModId)
class Generators {
  Config.init()
  Items.init()
  Blocks.init()
  Fluids.init()
  Machines.init()
  Recipes.init()
  Containers.init()
  NetworkHandler.init()

  FMLJavaModLoadingContext.get().getModEventBus.addListener(DataGeneration.onGatherData)
}

object Generators {
  final val ModId = "advgenerators"
}