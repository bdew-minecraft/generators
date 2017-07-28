/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.teslaOutput

import net.bdew.generators.config.Tuning
import net.bdew.generators.controllers.PoweredController
import net.bdew.lib.Misc
import net.bdew.lib.PimpVanilla._
import net.bdew.lib.block.BlockFace
import net.bdew.lib.capabilities.CapabilityProvider
import net.bdew.lib.data.DataSlotBoolean
import net.bdew.lib.data.base.UpdateKind
import net.bdew.lib.multiblock.data.OutputConfigPower
import net.bdew.lib.multiblock.interact.CIPowerProducer
import net.bdew.lib.multiblock.tile.{RSControllableOutput, TileOutput}
import net.darkhax.tesla.api.{ITeslaHolder, ITeslaProducer}
import net.minecraft.util.EnumFacing

class TileTeslaOutput extends TileOutput[OutputConfigPower] with RSControllableOutput with CapabilityProvider {
  val kind = "PowerOutput"

  val forcedSides = EnumFacing.values().map(f => f -> DataSlotBoolean("forced_" + f.name(), this, false).setUpdate(UpdateKind.WORLD, UpdateKind.SAVE)).toMap

  var outThisTick = Map.empty[EnumFacing, Float].withDefaultValue(0f)

  override val outputConfigType = classOf[OutputConfigPower]
  override def makeCfgObject(face: EnumFacing) = new OutputConfigPower("T")

  lazy val ratio = Tuning.getSection("Power").getFloat("T_MJ_Ratio")

  addCachedSidedCapability(Tesla.PRODUCER, new TeslaProducer(_))

  class TeslaProducer(side: EnumFacing) extends ITeslaProducer with ITeslaHolder {
    override def getStoredPower: Long =
      getCoreAs[PoweredController].map(c => (c.power.stored * ratio).floor.toLong) getOrElse 0L

    override def getCapacity: Long =
      getCoreAs[PoweredController].map(c => (c.power.capacity * ratio).floor.toLong) getOrElse 0L

    override def takePower(power: Long, simulated: Boolean): Long = {
      (for (cfg <- getCfg(side); core <- getCoreAs[CIPowerProducer] if checkCanOutput(cfg)) yield {
        val toReturn = (core.extract(power / ratio, true) * ratio).floor.toLong
        if (!simulated) {
          core.extract(toReturn / ratio, false) * ratio
          outThisTick += side -> (outThisTick(side) + toReturn)
        }
        toReturn
      }) getOrElse 0L
    }
  }

  override def canConnectToFace(d: EnumFacing): Boolean =
    forcedSides(d) || (Option(world.getTileEntity(pos.offset(d))) exists { tile =>
      tile.hasCapability(Tesla.CONSUMER, d.getOpposite)
    })

  override def doOutput(face: EnumFacing, cfg: OutputConfigPower) {
    for {
      core <- getCoreAs[CIPowerProducer] if checkCanOutput(cfg)
      tile <- Option(world.getTileEntity(pos.offset(face))) if tile.hasCapability(Tesla.CONSUMER, face.getOpposite)
      consumer <- Option(tile.getCapability(Tesla.CONSUMER, face.getOpposite))
    } {
      val canExtract = core.extract(Int.MaxValue, true)
      val injected = consumer.givePower((canExtract * ratio).floor.toLong, false)
      core.extract(injected / ratio, false)
      outThisTick += face -> (outThisTick(face) + injected)
    }
  }

  override def coreRemoved(): Unit = {
    super.coreRemoved()
    forcedSides.values.foreach(_ := false)
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
