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
import net.bdew.lib.multiblock.interact.CIFluidInput
import net.bdew.lib.multiblock.tile.TileModule
import net.bdew.pressure.api.{IPressureConnectableBlock, IPressureEject}
import net.minecraft.world.IBlockAccess
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.FluidStack

object BlockPressureInput extends BaseModule("PressureInput", "FluidInput", classOf[TilePressureInput])
with BlockNotifyUpdates with IPressureConnectableBlock {
  override def canConnectTo(world: IBlockAccess, x: Int, y: Int, z: Int, side: ForgeDirection) =
    getTE(world, x, y, z).getCore.isDefined
  override def isTraversable(world: IBlockAccess, x: Int, y: Int, z: Int) = false
}

class TilePressureInput extends TileModule with PressureModule with IPressureEject {
  val kind: String = "FluidInput"
  override def getCore = getCoreAs[CIFluidInput]

  override def eject(resource: FluidStack, direction: ForgeDirection, doEject: Boolean) = getCore map { core =>
    core.inputFluid(resource, doEject)
  } getOrElse 0

}
