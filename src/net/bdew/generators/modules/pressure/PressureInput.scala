/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.pressure

import net.bdew.generators.modules.BaseModule
import net.bdew.lib.multiblock.interact.CIFluidInput
import net.bdew.lib.multiblock.tile.TileModule
import net.bdew.pressure.api.{IPressureConnectableBlock, IPressureEject}
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraftforge.fluids.FluidStack

object BlockPressureInput extends BaseModule("pressure_input", "FluidInput", classOf[TilePressureInput]) with BlockNotifyUpdates with IPressureConnectableBlock {
  override def canConnectTo(world: IBlockAccess, pos: BlockPos, side: EnumFacing) =
    getTE(world, pos).exists(_.getCore.isDefined)
  override def isTraversable(world: IBlockAccess, pos: BlockPos) = false
}

class TilePressureInput extends TileModule with PressureModule with IPressureEject {
  val kind: String = "FluidInput"
  override def getCore = getCoreAs[CIFluidInput]

  override def eject(resource: FluidStack, direction: EnumFacing, doEject: Boolean) =
    getCore map { core =>
      val toFill = resource.copy()
      for (tank <- core.getInputTanks if toFill.amount > 0) {
        toFill.amount -= tank.fill(toFill.copy(), doEject)
      }
      resource.amount - toFill.amount
    } getOrElse 0
}
