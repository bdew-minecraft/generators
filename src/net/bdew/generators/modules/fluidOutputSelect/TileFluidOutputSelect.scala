/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.fluidOutputSelect

import net.bdew.lib.Misc
import net.bdew.lib.PimpVanilla._
import net.bdew.lib.capabilities.helpers.{FluidDrainMonitor, FluidHelper, FluidMultiHandler}
import net.bdew.lib.capabilities.{Capabilities, CapabilityProvider}
import net.bdew.lib.multiblock.data.OutputConfigFluidSlots
import net.bdew.lib.multiblock.interact.CIFluidOutputSelect
import net.bdew.lib.multiblock.tile.{RSControllableOutput, TileOutput}
import net.minecraft.util.EnumFacing
import net.minecraftforge.fluids.FluidStack

class TileFluidOutputSelect extends TileOutput[OutputConfigFluidSlots] with RSControllableOutput with CapabilityProvider {
  val kind: String = "FluidOutputSelect"

  override def getCore = getCoreAs[CIFluidOutputSelect]
  override val outputConfigType = classOf[OutputConfigFluidSlots]

  addCapabilityOption(Capabilities.CAP_FLUID_HANDLER) { side =>
    for {
      core <- getCore
      cfg <- getCfg(side) if checkCanOutput(cfg)
      slot <- Misc.asInstanceOpt(cfg.slot, classOf[core.outputSlotsDef.Slot])
    } yield {
      new FluidDrainMonitor(FluidMultiHandler.wrap(core.getOutputTanksForSlot(slot)), stack => addOutput(side, stack))
    }
  }

  def addOutput(side: EnumFacing, res: FluidStack) = {
    outThisTick += side -> (outThisTick.getOrElse(side, 0F) + res.amount)
  }

  var outThisTick = Map.empty[EnumFacing, Float]

  def updateOutput() {
    for {
      core <- getCore
      (side, amt) <- outThisTick
      cfg <- getCfg(side)
    } {
      cfg.updateAvg(amt)
      core.outputConfig.updated()
    }
    outThisTick = Map.empty
  }

  serverTick.listen(updateOutput)

  override def canConnectToFace(d: EnumFacing) =
    getCore.isDefined && FluidHelper.hasFluidHandler(world, pos.offset(d), d.getOpposite)

  override def makeCfgObject(face: EnumFacing) = new OutputConfigFluidSlots(getCore.get.outputSlotsDef)

  override def doOutput(face: EnumFacing, cfg: OutputConfigFluidSlots) {
    for {
      core <- getCore if checkCanOutput(cfg)
      target <- FluidHelper.getFluidHandler(world, pos.offset(face), face.getOpposite)
      slot <- Misc.asInstanceOpt(cfg.slot, classOf[core.outputSlotsDef.Slot])
    } {
      for (handler <- core.getOutputTanksForSlot(slot)) {
        val filled = FluidHelper.pushFluid(handler, target)
        if (filled != null) addOutput(face, filled)
      }
    }
  }
}
