/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.rfOutput

import cofh.redstoneflux.api.{IEnergyProvider, IEnergyReceiver}
import net.bdew.generators.config.Tuning
import net.bdew.generators.controllers.PoweredController
import net.bdew.lib.Misc
import net.bdew.lib.PimpVanilla._
import net.bdew.lib.block.BlockFace
import net.bdew.lib.multiblock.data.OutputConfigPower
import net.bdew.lib.multiblock.interact.CIPowerProducer
import net.bdew.lib.multiblock.misc.TileForcedOutput
import net.bdew.lib.multiblock.tile.{RSControllableOutput, TileOutput}
import net.minecraft.util.EnumFacing

class TileRfOutput extends TileOutput[OutputConfigPower] with RSControllableOutput with IEnergyProvider with TileForcedOutput {
  val kind = "PowerOutput"

  var outThisTick = Map.empty[EnumFacing, Float].withDefaultValue(0f)

  override val outputConfigType = classOf[OutputConfigPower]
  override def makeCfgObject(face: EnumFacing) = new OutputConfigPower("RF")

  val ratio = Tuning.getSection("Power").getFloat("RF_MJ_Ratio")

  override def canConnectEnergy(p1: EnumFacing) = true

  override def getEnergyStored(from: EnumFacing): Int =
    getCoreAs[PoweredController].map(c => (c.power.stored * ratio).toInt) getOrElse 0

  override def getMaxEnergyStored(from: EnumFacing): Int =
    getCoreAs[PoweredController].map(c => (c.power.capacity * ratio).toInt) getOrElse 0

  override def extractEnergy(from: EnumFacing, maxExtract: Int, simulate: Boolean): Int = {
    (for (cfg <- getCfg(from); core <- getCoreAs[CIPowerProducer] if checkCanOutput(cfg)) yield {
      val toReturn = (core.extract(maxExtract / ratio, true) * ratio).floor.toInt
      if (!simulate) {
        core.extract(toReturn / ratio, false) * ratio
        outThisTick += from -> (outThisTick(from) + toReturn)
      }
      toReturn
    }) getOrElse 0
  }

  override def canConnectToFace(d: EnumFacing): Boolean =
    forcedSides(d) || world.getTileSafe[IEnergyReceiver](pos.offset(d)).exists(t => t.canConnectEnergy(d.getOpposite))

  override def doOutput(face: EnumFacing, cfg: OutputConfigPower) {
    for {
      core <- getCoreAs[CIPowerProducer] if checkCanOutput(cfg)
      tile <- world.getTileSafe[IEnergyReceiver](pos.offset(face))
    } {
      val canExtract = core.extract(Int.MaxValue, true)
      val injected = tile.receiveEnergy(face.getOpposite, (canExtract * ratio).toInt, false)
      core.extract(injected / ratio, false)
      outThisTick += face -> (outThisTick(face) + injected)
    }
  }

  serverTick.listen(() => {
    getCore foreach { core =>
      var updated = false
      for {
        side <- EnumFacing.values()
        oNum <- core.outputFaces.get(BlockFace(pos, side))
        cfgGen <- core.outputConfig.get(oNum)
        cfg <- Misc.asInstanceOpt(cfgGen, outputConfigType)
      } {
        cfg.updateAvg(outThisTick(side))
        updated = true
      }
      if (updated) {
        core.outputFaces.updated()
        outThisTick = outThisTick.empty
      }
    }
  })
}
