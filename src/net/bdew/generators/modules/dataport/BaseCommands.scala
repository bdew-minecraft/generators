/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.dataport

import net.bdew.lib.computers._
import net.bdew.lib.data.DataSlotTankBase
import net.bdew.lib.multiblock.tile.TileController
import net.bdew.lib.resource.{DataSlotResource, FluidResource, ItemResource, Resource}

import scala.reflect.ClassTag

class BaseCommands[T <: TileController : ClassTag] extends ModuleCommandHandler[T, TileDataPort] {
  command("isConnected", direct = true) { ctx =>
    ctx.tile.getCoreAs[T].isDefined
  }

  def tankInfo(tank: DataSlotTankBase): Result = {
    Result.Map(
      "fluid" -> (if (tank.getFluid != null) Result(tank.getFluid.getFluid.getName) else Result.Null),
      "amount" -> tank.getFluidAmount,
      "capacity" -> tank.getCapacity
    )
  }

  def resourceInfo(resource: DataSlotResource): Result = {
    resource.resource match {
      case Some(Resource(ItemResource(item, meta), amt)) =>
        Result.Map(
          "kind" -> "item",
          "item" -> item.getRegistryName.toString,
          "meta" -> meta,
          "amount" -> amt,
          "capacity" -> resource.getEffectiveCapacity
        )
      case Some(Resource(FluidResource(fluid), amt)) =>
        Result.Map(
          "kind" -> "fluid",
          "fluid" -> fluid.getName,
          "amount" -> amt,
          "capacity" -> resource.getEffectiveCapacity
        )
      case None =>
        Result.Map(
          "kind" -> "empty"
        )
      case _ =>
        Result.Map(
          "kind" -> "unknown"
        )
    }
  }
}






