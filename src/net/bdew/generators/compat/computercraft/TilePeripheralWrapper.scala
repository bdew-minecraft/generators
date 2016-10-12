/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.compat.computercraft

import dan200.computercraft.api.lua.{ILuaContext, LuaException}
import dan200.computercraft.api.peripheral.{IComputerAccess, IPeripheral}
import net.bdew.lib.async.{Async, ServerTickExecutionContext}
import net.bdew.lib.computers._
import net.minecraft.tileentity.TileEntity

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

case class TilePeripheralWrapper[T <: TileEntity](kind: String, commands: TileCommandHandler[T], tile: T) extends IPeripheral {
  var computers = Set.empty[IComputerAccess]

  override def getType: String = kind

  override def detach(computer: IComputerAccess): Unit =
    if (computers.contains(computer))
      computers -= computer

  override def attach(computer: IComputerAccess): Unit =
    computers += computer

  override def equals(other: IPeripheral): Boolean =
    other match {
      case x: TilePeripheralWrapper[T] => x.tile == tile && x.commands == commands & x.kind == kind
      case _ => false
    }

  override def getMethodNames: Array[String] = commands.commandNames

  case class CCResultConverter(computer: IComputerAccess, context: ILuaContext) extends SimpleResultConverter {
    override def handleFuture(future: Future[Result]) = encode(waitForFuture(context, computer, future).get)
  }

  private def waitForFuture[R](ctx: ILuaContext, comp: IComputerAccess, future: Future[R]): Try[R] = {
    future.onComplete(x => comp.queueEvent("bdew.wakeup", Array.empty))(ServerTickExecutionContext)
    while (!future.isCompleted) {
      ctx.pullEventRaw("bdew.wakeup")
    }
    future.value.get
  }

  override def callMethod(computer: IComputerAccess, context: ILuaContext, method: Int, arguments: Array[AnyRef]): Array[AnyRef] = {
    val handler = commands.commands(commands.idToCommand(method))
    val ctx = CallContext(tile, arguments)
    val result = if (handler.isDirect) {
      Try(handler.apply(ctx))
    } else {
      waitForFuture(context, computer, Async.inServerThread(handler.apply(ctx)))
    }
    result match {
      case Success(null) => null
      case Success(v) => CCResultConverter(computer, context).wrap(v)
      case Failure(e: ParameterErrorException) =>
        throw new LuaException("Usage: %s(%s)".format(commands.idToCommand(method), e.params.map(_.name).mkString(", ")))
      case Failure(e: ComputerException) =>
        throw new LuaException(e.getMessage)
      case Failure(t) => throw t
    }
  }
}
