/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.gasInput

import mekanism.api.gas.{Gas, GasStack, ITubeConnection}
import net.bdew.lib.capabilities.CapabilityProvider
import net.bdew.lib.multiblock.interact.CIFluidInput
import net.bdew.lib.multiblock.tile.TileModule
import net.minecraft.util.EnumFacing
import net.minecraftforge.fluids.FluidStack

class TileGasInput extends TileModule with CapabilityProvider with GasHandlerProxy {
  val kind: String = "FluidInput"

  override def getCore = getCoreAs[CIFluidInput]

  object gasHandler extends BaseGasHandler with ITubeConnection {
    override def canTubeConnect(side: EnumFacing): Boolean = true
    override def canReceiveGas(side: EnumFacing, kind: Gas): Boolean = kind == null || kind.getFluid != null
    override def receiveGas(side: EnumFacing, stack: GasStack, doTransfer: Boolean): Int = {
      if (!worldObj.isRemote && stack != null && stack.getGas != null && stack.getGas.getFluid != null && stack.amount > 0) {
        val fluid = new FluidStack(stack.getGas.getFluid, stack.amount)
        for {
          core <- getCore
          tank <- core.getInputTanks
        } {
          val filled = tank.fill(fluid, true)
          if (filled > 0) return filled
        }
      }
      return 0
    }
  }

  addCapability(GasSupport.CAP_GAS_HANDLER, gasHandler)
  addCapability(GasSupport.CAP_TUBE_CONNECTION, gasHandler)
}
