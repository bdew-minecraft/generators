package net.bdew.generators.fluids

import net.bdew.generators.Generators
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fluids.FluidAttributes

object Syngas {
  val attributes: FluidAttributes.Builder =
    FluidAttributes.builder(
      new ResourceLocation(Generators.ModId, "blocks/syngas/still"),
      new ResourceLocation(Generators.ModId, "blocks/syngas/flowing")
    )
      .gaseous()
      .density(-10)
}
