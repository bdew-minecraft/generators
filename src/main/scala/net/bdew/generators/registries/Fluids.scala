package net.bdew.generators.registries

import net.bdew.generators.fluids._
import net.bdew.lib.managers.FluidManager
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.{FluidTags, TagKey}
import net.minecraft.world.level.material.Fluid
import net.minecraftforge.fluids.ForgeFlowingFluid

object Fluids extends FluidManager(Blocks, Items) {
  val syngas: FluidDef[ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing] =
    define("syngas", Syngas.attributes,
      new ForgeFlowingFluid.Source(_),
      new ForgeFlowingFluid.Flowing(_),
    )

  val steam: FluidDef[ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing] =
    define("steam", Steam.attributes,
      new ForgeFlowingFluid.Source(_),
      new ForgeFlowingFluid.Flowing(_),
    )

  val steamTag: TagKey[Fluid] = FluidTags.create(new ResourceLocation("forge:steam"))
}