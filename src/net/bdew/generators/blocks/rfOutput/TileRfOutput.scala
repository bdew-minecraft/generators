/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/generators/master/MMPL-1.0.txt
 */

package net.bdew.generators.blocks.rfOutput

import cofh.api.energy.IEnergyHandler
import net.bdew.generators.config.Tuning
import net.bdew.lib.multiblock.data.{OutputConfig, OutputConfigPower}
import net.bdew.lib.multiblock.interact.CIPowerProducer
import net.bdew.lib.multiblock.tile.TileOutput
import net.minecraftforge.common.util.ForgeDirection

class TileRfOutput extends TileOutput with IEnergyHandler {
  val kind = "PowerOutput"
  val unit = "RF"

  val ratio = Tuning.getSection("Power").getFloat("RF_MJ_Ratio")

  override def receiveEnergy(from: ForgeDirection, maxReceive: Int, simulate: Boolean): Int = 0
  override def extractEnergy(from: ForgeDirection, maxExtract: Int, simulate: Boolean): Int = 0
  override def canConnectEnergy(p1: ForgeDirection) = true
  override def getEnergyStored(from: ForgeDirection): Int = 0
  override def getMaxEnergyStored(from: ForgeDirection): Int = 0

  def canConnectoToFace(d: ForgeDirection): Boolean = {
    val tile = mypos.neighbour(d).getTile[IEnergyHandler](worldObj).getOrElse(return false)
    return tile.canConnectEnergy(d.getOpposite)
  }

  def doOutput(face: ForgeDirection, cfg: OutputConfig) {
    getCoreAs[CIPowerProducer] map { core =>
      val out = if (checkCanOutput(cfg.asInstanceOf[OutputConfigPower])) {
        mypos.neighbour(face).getTile[IEnergyHandler](worldObj) map { tile =>
          val canExtract = core.extract(Int.MaxValue, true)
          val injected = tile.receiveEnergy(face.getOpposite, (canExtract * ratio).toInt, false)
          core.extract(injected / ratio, false)
          injected
        } getOrElse 0
      } else 0
      cfg.asInstanceOf[OutputConfigPower].updateAvg(out)
      core.outputConfig.updated()
    }
  }
}
