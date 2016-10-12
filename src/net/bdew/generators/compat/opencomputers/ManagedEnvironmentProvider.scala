/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.compat.opencomputers

import li.cil.oc.api.driver.NamedBlock
import li.cil.oc.api.machine.{Arguments, Context, LimitReachedException}
import li.cil.oc.api.network.{ManagedPeripheral, Visibility}
import li.cil.oc.api.{Network, prefab}
import net.bdew.lib.computers.{CallContext, ComputerException, ParameterErrorException, TileCommandHandler}
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.relauncher.Side

import scala.util.{Failure, Success, Try}

class ManagedEnvironmentProvider[T <: TileEntity](kind: String, commands: TileCommandHandler[T], tile: T) extends prefab.ManagedEnvironment with ManagedPeripheral with NamedBlock {
  setNode(Network.newNode(this, Visibility.Network).withComponent(kind).create())
  override def priority(): Int = 0
  override def preferredName(): String = kind
  override def methods(): Array[String] = commands.commandNames
  override def invoke(method: String, context: Context, args: Arguments): Array[AnyRef] = {
    val handler = commands.commands(method)

    // Direct call to a non-direct command - tell OC to re-call in server thread
    if (!handler.isDirect && FMLCommonHandler.instance().getEffectiveSide != Side.SERVER)
      throw new LimitReachedException

    val ctx = CallContext(tile, args.toArray)
    Try(handler.apply(ctx)) match {
      case Success(null) => null
      case Success(v) => OCResultConverter.wrap(v)
      case Failure(e: ParameterErrorException) =>
        throw new ComputerException("Usage: %s(%s)".format(method, e.params.map(_.name).mkString(", ")))
      case Failure(t) => throw t
    }
  }
}
