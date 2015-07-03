/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.gasInput

import mekanism.api.gas.{Gas, GasStack, IGasHandler, ITubeConnection}
import net.bdew.lib.multiblock.interact.CIFluidInput
import net.bdew.lib.multiblock.tile.TileModule
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.FluidStack

class TileGasInput extends TileModule with IGasHandler with ITubeConnection {
  val kind: String = "FluidInput"

  override def getCore = getCoreAs[CIFluidInput]

  override def canTubeConnect(side: ForgeDirection): Boolean = getCore.isDefined

  override def drawGas(side: ForgeDirection, amount: Int): GasStack = null
  override def drawGas(side: ForgeDirection, amount: Int, doTransfer: Boolean): GasStack = null
  override def canDrawGas(side: ForgeDirection, gas: Gas): Boolean = false

  override def canReceiveGas(side: ForgeDirection, gas: Gas): Boolean =
    gas != null && gas.hasFluid && (getCore exists (_.canInputFluid(gas.getFluid)))

  override def receiveGas(side: ForgeDirection, stack: GasStack): Int = receiveGas(side, stack, true)
  override def receiveGas(side: ForgeDirection, stack: GasStack, doTransfer: Boolean): Int =
    if (stack != null && canReceiveGas(side, stack.getGas))
      getCore map (_.inputFluid(new FluidStack(stack.getGas.getFluid, stack.amount), doTransfer)) getOrElse 0
    else
      0
}
