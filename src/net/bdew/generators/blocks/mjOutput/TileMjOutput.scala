/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.blocks.mjOutput

import buildcraft.api.power.{IPowerEmitter, IPowerReceptor, PowerHandler}
import net.bdew.lib.multiblock.data.OutputConfigPower
import net.bdew.lib.multiblock.interact.CIPowerProducer
import net.bdew.lib.multiblock.tile.{RSControllableOutput, TileOutput}
import net.minecraftforge.common.util.ForgeDirection

class TileMjOutput extends TileOutput[OutputConfigPower] with RSControllableOutput with IPowerReceptor with IPowerEmitter {
  val kind = "PowerOutput"

  override val outputConfigType = classOf[OutputConfigPower]
  override def makeCfgObject(face: ForgeDirection) = new OutputConfigPower("MJ")

  def canEmitPowerFrom(side: ForgeDirection): Boolean = true
  def getPowerReceiver(side: ForgeDirection) = null
  def doWork(workProvider: PowerHandler) {}
  def getWorld = worldObj

  def canConnectoToFace(d: ForgeDirection): Boolean = {
    val tile = mypos.neighbour(d).getTile[IPowerReceptor](worldObj).getOrElse(return false)
    val pr = tile.getPowerReceiver(d)
    return pr != null
  }

  def doOutput(face: ForgeDirection, cfg: OutputConfigPower) {
    getCoreAs[CIPowerProducer] map { core =>
      val out = if (checkCanOutput(cfg)) {
        mypos.neighbour(face).getTile[IPowerReceptor](worldObj) flatMap (x => Option(x.getPowerReceiver(face))) map { pr =>
          val canExtract = core.extract(pr.getMaxEnergyReceived.toFloat, true)
          if (canExtract >= pr.getMinEnergyReceived) {
            val injected = pr.receiveEnergy(PowerHandler.Type.ENGINE, canExtract, face.getOpposite).toFloat
            core.extract(injected, false)
            injected
          } else 0
        } getOrElse 0F
      } else 0
      cfg.updateAvg(out)
      core.outputConfig.updated()
    }
  }
}
