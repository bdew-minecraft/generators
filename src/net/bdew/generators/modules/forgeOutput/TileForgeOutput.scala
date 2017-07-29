/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.forgeOutput

import net.bdew.generators.config.Tuning
import net.bdew.generators.controllers.PoweredController
import net.bdew.lib.Misc
import net.bdew.lib.PimpVanilla._
import net.bdew.lib.block.BlockFace
import net.bdew.lib.capabilities.CapabilityProvider
import net.bdew.lib.multiblock.data.OutputConfigPower
import net.bdew.lib.multiblock.interact.CIPowerProducer
import net.bdew.lib.multiblock.misc.TileForcedOutput
import net.bdew.lib.multiblock.tile.{RSControllableOutput, TileOutput}
import net.minecraft.util.EnumFacing
import net.minecraftforge.energy.{CapabilityEnergy, IEnergyStorage}

class TileForgeOutput extends TileOutput[OutputConfigPower] with RSControllableOutput with CapabilityProvider with TileForcedOutput {
  val kind = "PowerOutput"

  var outThisTick = Map.empty[EnumFacing, Float].withDefaultValue(0f)

  override val outputConfigType = classOf[OutputConfigPower]
  override def makeCfgObject(face: EnumFacing) = new OutputConfigPower("E")

  lazy val ratio = Tuning.getSection("Power").getFloat("Forge_MJ_Ratio")

  addCachedSidedCapability(CapabilityEnergy.ENERGY, new ForgePowerHandler(_))

  class ForgePowerHandler(side: EnumFacing) extends IEnergyStorage {
    override def getEnergyStored: Int =
      getCoreAs[PoweredController].map(c => (c.power.stored * ratio).floor.toInt) getOrElse 0
    override def getMaxEnergyStored: Int =
      getCoreAs[PoweredController].map(c => (c.power.capacity * ratio).floor.toInt) getOrElse 0

    override def canReceive: Boolean = false
    override def receiveEnergy(maxReceive: Int, simulate: Boolean): Int = 0

    override def canExtract: Boolean = true
    override def extractEnergy(maxExtract: Int, simulate: Boolean): Int = {
      (for (cfg <- getCfg(side); core <- getCoreAs[CIPowerProducer] if checkCanOutput(cfg)) yield {
        val toReturn = (core.extract(maxExtract / ratio, true) * ratio).floor.toInt
        if (!simulate) {
          core.extract(toReturn / ratio, false) * ratio
          outThisTick += side -> (outThisTick(side) + toReturn)
        }
        toReturn
      }) getOrElse 0
    }
  }

  override def canConnectToFace(d: EnumFacing): Boolean =
    forcedSides(d) || (Option(world.getTileEntity(pos.offset(d))) exists { tile =>
      tile.hasCapability(CapabilityEnergy.ENERGY, d.getOpposite)
    })

  override def doOutput(face: EnumFacing, cfg: OutputConfigPower) {
    for {
      core <- getCoreAs[CIPowerProducer] if checkCanOutput(cfg)
      tile <- Option(world.getTileEntity(pos.offset(face))) if tile.hasCapability(CapabilityEnergy.ENERGY, face.getOpposite)
      target <- Option(tile.getCapability(CapabilityEnergy.ENERGY, face.getOpposite))
    } {
      val canExtract = core.extract(Int.MaxValue, true)
      val injected = target.receiveEnergy((canExtract * ratio).floor.toInt, false)
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
