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
import net.bdew.lib.PimpVanilla._
import net.bdew.lib.multiblock.data.OutputConfigFluidSlots
import net.bdew.lib.multiblock.interact.CIFluidOutputSelect
import net.bdew.lib.multiblock.tile.{RSControllableOutput, TileOutput}
import net.minecraft.util.EnumFacing
import net.minecraftforge.fluids.{Fluid, FluidStack, IFluidHandler}

class TileFluidOutputSelect extends TileOutput[OutputConfigFluidSlots] with RSControllableOutput with IFluidHandler {
  val kind: String = "FluidOutputSelect"

  override def getCore = getCoreAs[CIFluidOutputSelect]
  override val outputConfigType = classOf[OutputConfigFluidSlots]

  override def canConnectToFace(d: EnumFacing) =
    getCore exists { core =>
      worldObj.getTileEntity(pos.offset(d)).isInstanceOf[IFluidHandler]
    }

  override def fill(from: EnumFacing, resource: FluidStack, doFill: Boolean) = 0
  override def canFill(from: EnumFacing, fluid: Fluid) = false

  override def getTankInfo(from: EnumFacing) =
    getCore map (_.getTankInfo) getOrElse Array.empty

  override def makeCfgObject(face: EnumFacing) = new OutputConfigFluidSlots(getCore.get.outputSlotsDef)

  override def doOutput(face: EnumFacing, cfg: OutputConfigFluidSlots) {
    val outputted = if (checkCanOutput(cfg)) {
      for {
        core <- getCore
        target <- worldObj.getTileSafe[IFluidHandler](pos.offset(face))
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

  override def canDrain(from: EnumFacing, fluid: Fluid) = false

  override def drain(from: EnumFacing, resource: FluidStack, doDrain: Boolean) = null
  override def drain(from: EnumFacing, maxDrain: Int, doDrain: Boolean) = null
}
