/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license
 */

package net.bdew.generators.waila

import net.bdew.generators.controllers.exchanger.TileExchangerController
import net.bdew.lib.resource.{FluidResource, ResourceKind}
import net.bdew.lib.{DecFormat, Misc}
import net.minecraft.nbt.NBTTagCompound

object HeatExchangerDataProvider extends BaseControllerDataProvider(classOf[TileExchangerController]) {
  def formatFlowRate(r: Option[ResourceKind], v: Double, fmt: String) = r match {
    case Some(x: FluidResource) =>
      Some(Misc.toLocalF(fmt, x.getLocalizedName + " " + DecFormat.short(v), "mB"))
    case Some(x: ResourceKind) =>
      Some(Misc.toLocalF(fmt, DecFormat.short(v), x.getLocalizedName))
    case _ => None
  }

  override def getBodyStringsFromData(te: TileExchangerController, data: NBTTagCompound) = {
    loadData(te, data)
    val dsList = for {
      ds <- List(te.heaterIn, te.heaterOut, te.coolerIn, te.coolerOut)
      res <- ds.resource
    } yield res.kind.getLocalizedName + " " + res.kind.getFormattedString(res.amount, ds.getEffectiveCapacity)
    (dsList
      ++ List(Misc.toLocal("advgenerators.label.exchanger.heat") + ": " + DecFormat.short(te.heat.value) + " HU")
      ++ formatFlowRate(te.lastInput, te.inputRate.average, "advgenerators.waila.turbine.consuming")
      ++ formatFlowRate(te.lastOutput, te.outputRate.average, "advgenerators.waila.turbine.producing")
      )
  }
}
