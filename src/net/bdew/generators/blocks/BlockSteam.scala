/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.blocks

import net.bdew.generators.Generators
import net.minecraft.block.material.{MapColor, MaterialLiquid}
import net.minecraftforge.fluids.{BlockFluidClassic, Fluid}

object MaterialSteam extends MaterialLiquid(MapColor.SNOW)

class BlockSteam(fluid: Fluid) extends BlockFluidClassic(fluid, MaterialSteam) {
  setRegistryName(Generators.modId, "steam")
  setUnlocalizedName(Generators.modId + ".steam")
}
