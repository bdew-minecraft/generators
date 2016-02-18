/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.rfOutput

import cofh.api.energy.{IEnergyHandler, IEnergyReceiver}
import net.bdew.generators.config.Tuning
import net.bdew.generators.controllers.PoweredController
import net.bdew.lib.PimpVanilla._
import net.bdew.lib.multiblock.data.OutputConfigPower
import net.bdew.lib.multiblock.interact.CIPowerProducer
import net.bdew.lib.multiblock.tile.{RSControllableOutput, TileOutput}
import net.minecraft.util.EnumFacing

class TileRfOutput extends TileOutput[OutputConfigPower] with RSControllableOutput with IEnergyHandler {
  val kind = "PowerOutput"

  override val outputConfigType = classOf[OutputConfigPower]
  override def makeCfgObject(face: EnumFacing) = new OutputConfigPower("RF")

  val ratio = Tuning.getSection("Power").getFloat("RF_MJ_Ratio")

  override def canConnectEnergy(p1: EnumFacing) = true
  override def getEnergyStored(from: EnumFacing): Int =
    getCoreAs[PoweredController].map(c => (c.power.stored * ratio).toInt) getOrElse 0
  override def getMaxEnergyStored(from: EnumFacing): Int =
    getCoreAs[PoweredController].map(c => (c.power.capacity * ratio).toInt) getOrElse 0

  override def canConnectToFace(d: EnumFacing): Boolean =
    worldObj.getTileSafe[IEnergyReceiver](pos.offset(d)).exists(t => t.canConnectEnergy(d.getOpposite))

  override def doOutput(face: EnumFacing, cfg: OutputConfigPower) {
    getCoreAs[CIPowerProducer] foreach { core =>
      val out = if (checkCanOutput(cfg)) {
        worldObj.getTileSafe[IEnergyReceiver](pos.offset(face)) map { tile =>
          val canExtract = core.extract(Int.MaxValue, true)
          val injected = tile.receiveEnergy(face.getOpposite, (canExtract * ratio).toInt, false)
          core.extract(injected / ratio, false)
          injected
        } getOrElse 0
      } else 0
      cfg.updateAvg(out)
      core.outputConfig.updated()
    }
  }
}
