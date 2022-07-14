package net.bdew.generators

import net.bdew.generators.registries.{Blocks, Fluids}
import net.bdew.lib.multiblock.render.MultiblockRenderHelper
import net.minecraft.client.renderer.{ItemBlockRenderTypes, RenderType}
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.ModelEvent.RegisterGeometryLoaders
import net.minecraftforge.client.event.RegisterColorHandlersEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent

@Mod.EventBusSubscriber(value = Array(Dist.CLIENT), modid = Generators.ModId, bus = Bus.MOD)
object ClientHandler {
  @SubscribeEvent
  def registerLoaders(ev: RegisterGeometryLoaders): Unit = {
    ev.register("multiblock_model", CustomModels.multiblockModelLoader)
  }

  @SubscribeEvent
  def registerBlockColors(ev: RegisterColorHandlersEvent.Block): Unit = {
    MultiblockRenderHelper.setupColors(ev, Blocks.all.map(_.get()), GeneratorsResourceProvider)
  }

  @SubscribeEvent
  def clientSetup(ev: FMLClientSetupEvent): Unit = {
    ev.enqueueWork(new Runnable {
      override def run(): Unit = {
        ItemBlockRenderTypes.setRenderLayer(Fluids.syngas.source.get(), RenderType.translucent())
        ItemBlockRenderTypes.setRenderLayer(Fluids.syngas.flowing.get(), RenderType.translucent())
      }
    })
  }
}
