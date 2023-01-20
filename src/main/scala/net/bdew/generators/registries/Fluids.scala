package net.bdew.generators.registries

import net.bdew.generators.Generators
import net.bdew.lib.managers.FluidManager
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.{FluidTags, TagKey}
import net.minecraft.world.level.material.Fluid
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions
import net.minecraftforge.fluids.{FluidType, ForgeFlowingFluid}

import java.util.function.Consumer

object Fluids extends FluidManager(Blocks, Items) {
  def gasProps: FluidType.Properties = FluidType.Properties.create()
    .density(-10)

  val syngas: Fluids.FluidDef[FluidType, ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing] =
    define("syngas", () => new FluidType(gasProps.descriptionId("block.advgenerators.syngas")) {

      override def initializeClient(consumer: Consumer[IClientFluidTypeExtensions]): Unit = {
        consumer.accept(new IClientFluidTypeExtensions() {
          private val MILK_STILL = new ResourceLocation(Generators.ModId, "block/syngas/still")
          private val MILK_FLOW = new ResourceLocation(Generators.ModId, "block/syngas/flowing")
          override def getStillTexture: ResourceLocation = MILK_STILL
          override def getFlowingTexture: ResourceLocation = MILK_FLOW
        })
      }
    },
      new ForgeFlowingFluid.Source(_),
      new ForgeFlowingFluid.Flowing(_),
    )

  val steam: Fluids.FluidDef[FluidType, ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing] =
    define("steam", () => new FluidType(gasProps.descriptionId("block.advgenerators.steam")) {
      override def initializeClient(consumer: Consumer[IClientFluidTypeExtensions]): Unit = {
        consumer.accept(new IClientFluidTypeExtensions() {
          private val STILL = new ResourceLocation(Generators.ModId, "block/steam/still")
          private val FLOW = new ResourceLocation(Generators.ModId, "block/steam/flowing")
          override def getStillTexture: ResourceLocation = STILL
          override def getFlowingTexture: ResourceLocation = FLOW
        })
      }
    },
      new ForgeFlowingFluid.Source(_),
      new ForgeFlowingFluid.Flowing(_),
    )

  val steamTag: TagKey[Fluid] = FluidTags.create(new ResourceLocation("forge:steam"))
}