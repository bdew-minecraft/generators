package net.bdew.generators.fluids

import net.bdew.generators.Generators
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fluids.FluidAttributes

object Steam {
  val attributes: FluidAttributes.Builder = FluidAttributes.builder(
    new ResourceLocation(Generators.ModId, "blocks/steam/still"),
    new ResourceLocation(Generators.ModId, "blocks/steam/flowing")
  ).gaseous().density(-10)
}
