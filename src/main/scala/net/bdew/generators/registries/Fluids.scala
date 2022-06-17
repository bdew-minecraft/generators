package net.bdew.generators.registries

import net.bdew.lib.managers.FluidManager
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.{FluidTags, TagKey}
import net.minecraft.world.level.material.Fluid
import net.minecraftforge.fluids.{FluidType, ForgeFlowingFluid}

object Fluids extends FluidManager(Blocks, Items) {
  def gasProps: FluidType.Properties = FluidType.Properties.create()
    .density(-10)

  val syngas: Fluids.FluidDef[FluidType, ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing] =
    define("syngas", () => new FluidType(gasProps.descriptionId("block.advgenerators.syngas")),
      new ForgeFlowingFluid.Source(_),
      new ForgeFlowingFluid.Flowing(_),
    )

  val steam: Fluids.FluidDef[FluidType, ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing] =
    define("steam", () => new FluidType(gasProps.descriptionId("block.advgenerators.steam")),
      new ForgeFlowingFluid.Source(_),
      new ForgeFlowingFluid.Flowing(_),
    )

  val steamTag: TagKey[Fluid] = FluidTags.create(new ResourceLocation("forge:steam"))
}