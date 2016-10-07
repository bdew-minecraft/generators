/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.euOutput

import ic2.api.energy.EnergyNet
import ic2.api.energy.tile.{IEnergyAcceptor, IEnergySource, IEnergyTile}
import net.bdew.generators.compat.Ic2EnetRegister
import net.bdew.generators.config.Tuning
import net.bdew.lib.PimpVanilla._
import net.bdew.lib.data.DataSlotDouble
import net.bdew.lib.data.base.UpdateKind
import net.bdew.lib.multiblock.data.OutputConfigPower
import net.bdew.lib.multiblock.interact.CIPowerProducer
import net.bdew.lib.multiblock.tile.{RSControllableOutput, TileOutput}
import net.bdew.lib.rotate.Properties
import net.minecraft.util.EnumFacing

abstract class TileEuOutputBase(val maxOutput: Int, val tier: Int) extends TileOutput[OutputConfigPower] with RSControllableOutput with IEnergySource with Ic2EnetRegister {
  val kind = "PowerOutput"
  val ratio = Tuning.getSection("Power").getFloat("EU_MJ_Ratio")

  val buffer = DataSlotDouble("buffer", this).setUpdate(UpdateKind.SAVE)

  override val outputConfigType = classOf[OutputConfigPower]
  override def makeCfgObject(face: EnumFacing) = new OutputConfigPower("EU")

  def getFacing = worldObj.getBlockState(pos).getValue(Properties.FACING)

  override def canConnectToFace(d: EnumFacing): Boolean = {
    if (d != getFacing) return false
    EnergyNet.instance.getSubTile(worldObj, pos.offset(d)) match {
      case null => false
      case x: IEnergyAcceptor => x.acceptsEnergyFrom(this, d.getOpposite)
      case x: IEnergyTile => true
    }
  }

  override def onConnectionsChanged(added: Set[EnumFacing], removed: Set[EnumFacing]) {
    sendUnload()
  }

  override def emitsEnergyTo(iEnergyAcceptor: IEnergyAcceptor, face: EnumFacing): Boolean =
    getCore.isDefined && face == getFacing

  def getCfg: Option[OutputConfigPower] = {
    val core = getCoreAs[CIPowerProducer].getOrElse(return None)
    val oNum = core.outputFaces.find(_._1.pos == pos).getOrElse(return None)._2
    Some(core.outputConfig.getOrElse(oNum, return None).asInstanceOf[OutputConfigPower])
  }

  override def getOfferedEnergy: Double = {
    if (buffer > 0 && checkCanOutput(getCfg.getOrElse(return 0)))
      Math.min(buffer, maxOutput)
    else
      0
  }

  override def drawEnergy(amount: Double) {
    buffer := buffer - amount
  }

  override def getSourceTier = tier

  def doOutput(face: EnumFacing, cfg: OutputConfigPower): Unit = {
    getCoreAs[CIPowerProducer] foreach { core =>
      if (buffer < maxOutput) {
        val extracted = core.extract(maxOutput / ratio, false) * ratio
        buffer += extracted
        cfg.updateAvg(extracted)
        core.outputConfig.updated()
      } else {
        cfg.updateAvg(0)
        core.outputConfig.updated()
      }
    }
  }
}

class TileEuOutputLV extends TileEuOutputBase(32, 1)

class TileEuOutputMV extends TileEuOutputBase(128, 2)

class TileEuOutputHV extends TileEuOutputBase(512, 3)

class TileEuOutputEV extends TileEuOutputBase(2048, 4)

class TileEuOutputIV extends TileEuOutputBase(8192, 5)
