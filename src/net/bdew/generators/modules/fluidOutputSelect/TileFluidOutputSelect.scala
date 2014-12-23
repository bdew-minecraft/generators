/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.fluidOutputSelect

import net.bdew.lib.Misc
import net.bdew.lib.multiblock.data.OutputConfigFluidSlots
import net.bdew.lib.multiblock.interact.CIFluidOutputSelect
import net.bdew.lib.multiblock.tile.{RSControllableOutput, TileOutput}
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.{Fluid, FluidStack, IFluidHandler}

class TileFluidOutputSelect extends TileOutput[OutputConfigFluidSlots] with RSControllableOutput with IFluidHandler {
  val kind: String = "FluidOutputSelect"

  override def getCore = getCoreAs[CIFluidOutputSelect]
  override val outputConfigType = classOf[OutputConfigFluidSlots]

  override def canConnectoToFace(d: ForgeDirection) =
    getCore exists { core =>
      mypos.neighbour(d).getTile[IFluidHandler](worldObj).isDefined
    }

  override def fill(from: ForgeDirection, resource: FluidStack, doFill: Boolean) = 0
  override def canFill(from: ForgeDirection, fluid: Fluid) = false

  override def getTankInfo(from: ForgeDirection) =
    getCore map (_.getTankInfo) getOrElse Array.empty

  override def makeCfgObject(face: ForgeDirection) = new OutputConfigFluidSlots(getCore.get.outputSlotsDef)

  override def doOutput(face: ForgeDirection, cfg: OutputConfigFluidSlots) {
    val outputted = if (checkCanOutput(cfg)) {
      for {
        core <- getCore
        target <- mypos.neighbour(face).getTile[IFluidHandler](worldObj)
        tSlot <- Misc.asInstanceOpt(cfg.slot, classOf[core.outputSlotsDef.Slot])
        toSend <- Option(core.outputFluid(tSlot, Int.MaxValue, false))
      } yield {
        val filled = target.fill(face.getOpposite, toSend, true)
        if (filled > 0) {
          core.outputFluid(tSlot, filled, true)
          core.outputConfig.updated()
          filled
        } else 0
      }
    } else None
    cfg.updateAvg(outputted.getOrElse(0).toDouble)
  }

  override def canDrain(from: ForgeDirection, fluid: Fluid) = false

  override def drain(from: ForgeDirection, resource: FluidStack, doDrain: Boolean) = null
  override def drain(from: ForgeDirection, maxDrain: Int, doDrain: Boolean) = null
}
