/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/generators/master/MMPL-1.0.txt
 */

package net.bdew.generators.blocks.fluidInput

import net.bdew.lib.multiblock.interact.CIFluidInput
import net.bdew.lib.multiblock.tile.TileModule
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.{Fluid, FluidStack, FluidTankInfo, IFluidHandler}

class TileFluidInput extends TileModule with IFluidHandler {
  val kind: String = "FluidInput"

  override def getCore = getCoreAs[CIFluidInput]

  def fill(from: ForgeDirection, resource: FluidStack, doFill: Boolean): Int =
    getCore map (_.inputFluid(resource, doFill)) getOrElse 0

  def canFill(from: ForgeDirection, fluid: Fluid): Boolean =
    getCore exists (_.canInputFluid(fluid))

  def getTankInfo(from: ForgeDirection): Array[FluidTankInfo] =
    getCore map (_.getTankInfo) getOrElse Array.empty

  def canDrain(from: ForgeDirection, fluid: Fluid): Boolean = false
  def drain(from: ForgeDirection, resource: FluidStack, doDrain: Boolean): FluidStack = null
  def drain(from: ForgeDirection, maxDrain: Int, doDrain: Boolean): FluidStack = null
}
