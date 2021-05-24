package net.bdew.generators

import net.bdew.generators.registries.{Blocks, Fluids}
import net.bdew.lib.multiblock.render.MultiblockRenderHelper
import net.minecraft.client.renderer.{RenderType, RenderTypeLookup}
import net.minecraft.util.ResourceLocation
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoaderRegistry
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent

@Mod.EventBusSubscriber(value = Array(Dist.CLIENT), modid = Generators.ModId, bus = Bus.MOD)
object ClientHandler {
  @SubscribeEvent
  def registerLoaders(ev: ModelRegistryEvent): Unit = {
    ModelLoaderRegistry.registerLoader(
      new ResourceLocation("advgenerators", "multiblock_model"),
      CustomModels.multiblockModelLoader
    )
  }

  @SubscribeEvent
  def clientSetup(ev: FMLClientSetupEvent): Unit = {
    ev.enqueueWork(new Runnable {
      override def run(): Unit = {
        MultiblockRenderHelper.setup(Blocks.all.map(_.get()), GeneratorsResourceProvider)
        RenderTypeLookup.setRenderLayer(Fluids.syngas.source.get(), RenderType.translucent())
        RenderTypeLookup.setRenderLayer(Fluids.syngas.flowing.get(), RenderType.translucent())
      }
    })
  }
}
