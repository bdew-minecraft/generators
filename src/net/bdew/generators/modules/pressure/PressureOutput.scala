/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license
 */

package net.bdew.generators.modules.pressure

import net.bdew.generators.modules.BaseModule
import net.bdew.lib.Misc
import net.bdew.lib.multiblock.block.BlockOutput
import net.bdew.lib.multiblock.data.OutputConfigFluidSlots
import net.bdew.lib.multiblock.interact.CIFluidOutputSelect
import net.bdew.lib.multiblock.tile.{RSControllableOutput, TileOutput}
import net.bdew.pressure.api.{IPressureConnectableBlock, IPressureConnection, IPressureInject, PressureAPI}
import net.minecraft.world.IBlockAccess
import net.minecraftforge.common.util.ForgeDirection

object BlockPressureOutput extends BaseModule("PressureOutputSelect", "FluidOutputSelect", classOf[TilePressureOutput])
with BlockOutput[TilePressureOutput] with BlockNotifyUpdates with IPressureConnectableBlock {
  override def canConnectTo(world: IBlockAccess, x: Int, y: Int, z: Int, side: ForgeDirection) =
    getTE(world, x, y, z).getCore.isDefined
  override def isTraversable(world: IBlockAccess, x: Int, y: Int, z: Int) = false
  override def canConnectRedstone(world: IBlockAccess, x: Int, y: Int, z: Int, side: Int) = true
}

class TilePressureOutput extends TileOutput[OutputConfigFluidSlots] with PressureModule with RSControllableOutput with IPressureInject {
  val kind: String = "FluidOutputSelect"

  override val outputConfigType = classOf[OutputConfigFluidSlots]

  override def getCore = getCoreAs[CIFluidOutputSelect]

  override def canConnectToFace(d: ForgeDirection) = {
    val t = myPos.neighbour(d)
    PressureAPI.HELPER.canPipeConnectFrom(worldObj, t.x, t.y, t.z, d.getOpposite)
  }

  override def makeCfgObject(face: ForgeDirection) = new OutputConfigFluidSlots(getCore.get.outputSlotsDef)

  override def invalidateConnection(direction: ForgeDirection) = connections -= direction

  var connections = Map.empty[ForgeDirection, IPressureConnection]

  override def doOutput(face: ForgeDirection, cfg: OutputConfigFluidSlots) = {
    val outputted = if (checkCanOutput(cfg)) {
      if (!connections.isDefinedAt(face))
        connections ++= Option(PressureAPI.HELPER.recalculateConnectionInfo(this, face)) map { cObj => face -> cObj }
      for {
        core <- getCore
        tSlot <- Misc.asInstanceOpt(cfg.slot, classOf[core.outputSlotsDef.Slot])
        toSend <- Option(core.outputFluid(tSlot, Int.MaxValue, false))
        conn <- connections.get(face)
      } yield {
        val filled = conn.pushFluid(toSend, true)
        if (filled > 0) {
          core.outputFluid(tSlot, filled, true)
          core.outputConfig.updated()
          filled
        } else 0
      }
    } else None
    cfg.updateAvg(outputted.getOrElse(0).toDouble)
  }
}

