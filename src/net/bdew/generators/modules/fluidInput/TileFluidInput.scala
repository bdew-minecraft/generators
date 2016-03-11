/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.fluidInput

import net.bdew.lib.multiblock.interact.CIFluidInput
import net.bdew.lib.multiblock.tile.TileModule
import net.minecraft.util.EnumFacing
import net.minecraftforge.fluids.{Fluid, FluidStack, FluidTankInfo, IFluidHandler}

class TileFluidInput extends TileModule with IFluidHandler {
  val kind: String = "FluidInput"

  override def getCore = getCoreAs[CIFluidInput]

  def fill(from: EnumFacing, resource: FluidStack, doFill: Boolean): Int =
    if (resource != null)
      getCore map (_.inputFluid(resource, doFill)) getOrElse 0
    else 0

  def canFill(from: EnumFacing, fluid: Fluid): Boolean =
    getCore exists (_.canInputFluid(fluid))

  def getTankInfo(from: EnumFacing): Array[FluidTankInfo] =
    getCore map (_.getTankInfo) getOrElse Array.empty

  def canDrain(from: EnumFacing, fluid: Fluid): Boolean = false
  def drain(from: EnumFacing, resource: FluidStack, doDrain: Boolean): FluidStack = null
  def drain(from: EnumFacing, maxDrain: Int, doDrain: Boolean): FluidStack = null
}
