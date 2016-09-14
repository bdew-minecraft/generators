/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.gasInput

import mekanism.api.gas.{Gas, GasStack, IGasHandler, ITubeConnection}
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.capabilities.{Capability, CapabilityInject}

import scala.annotation.meta.setter

object GasSupport {
  @(CapabilityInject@setter)(classOf[IGasHandler])
  var CAP_GAS_HANDLER: Capability[IGasHandler] = _

  @(CapabilityInject@setter)(classOf[ITubeConnection])
  var CAP_TUBE_CONNECTION: Capability[ITubeConnection] = _

  def getGasHandler(world: World, pos: BlockPos, face: EnumFacing): Option[IGasHandler] = {
    val te = world.getTileEntity(pos)
    if (te == null) return None
    if (te.hasCapability(CAP_GAS_HANDLER, face))
      Option(te.getCapability(CAP_GAS_HANDLER, face))
    else if (te.isInstanceOf[IGasHandler])
      Some(te.asInstanceOf[IGasHandler])
    else None
  }
}

class BaseGasHandler extends IGasHandler with ITubeConnection {
  override def canDrawGas(side: EnumFacing, kind: Gas): Boolean = false
  override def drawGas(side: EnumFacing, amount: Int, doTransfer: Boolean): GasStack = null
  override def canReceiveGas(side: EnumFacing, kind: Gas): Boolean = false
  override def receiveGas(side: EnumFacing, stack: GasStack, doTransfer: Boolean): Int = 0
  override def canTubeConnect(side: EnumFacing): Boolean = true
}

trait GasHandlerProxy extends TileEntity with IGasHandler with ITubeConnection {
  private def getGasCap(side: EnumFacing): Option[IGasHandler] =
    if (hasCapability(GasSupport.CAP_GAS_HANDLER, side))
      Option(getCapability(GasSupport.CAP_GAS_HANDLER, side))
    else None

  private def getTubeCap(side: EnumFacing): Option[ITubeConnection] =
    if (hasCapability(GasSupport.CAP_TUBE_CONNECTION, side))
      Option(getCapability(GasSupport.CAP_TUBE_CONNECTION, side))
    else None

  override def canDrawGas(side: EnumFacing, kind: Gas): Boolean = getGasCap(side).exists(_.canDrawGas(side, kind))
  override def canReceiveGas(side: EnumFacing, kind: Gas): Boolean = getGasCap(side).exists(_.canReceiveGas(side, kind))

  override def drawGas(side: EnumFacing, amount: Int, doTransfer: Boolean): GasStack = getGasCap(side).map(_.drawGas(side, amount, doTransfer)).orNull
  override def receiveGas(side: EnumFacing, stack: GasStack, doTransfer: Boolean): Int = getGasCap(side).map(_.receiveGas(side, stack, doTransfer)).getOrElse(0)

  override def canTubeConnect(side: EnumFacing): Boolean = getTubeCap(side).exists(_.canTubeConnect(side))
}