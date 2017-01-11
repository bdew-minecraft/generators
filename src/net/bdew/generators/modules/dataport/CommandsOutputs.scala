/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.dataport

import java.util.Locale

import net.bdew.lib.computers.{PString, Result}
import net.bdew.lib.multiblock.data._
import net.bdew.lib.multiblock.interact.CIOutputFaces

trait CommandsOutputs {
  self: BaseCommands[_ <: CIOutputFaces] =>
  val outputNames = Map(
    "red" -> 0,
    "green" -> 1,
    "blue" -> 2,
    "yellow" -> 3,
    "cyan" -> 4,
    "purple" -> 5
  )

  command("getOutputs", direct = true) { ctx =>
    val configs = getCore(ctx).outputConfig
    for ((oName, oNum) <- outputNames) yield {
      oName -> (configs.get(oNum) match {
        case Some(x: OutputConfigFluidSlots) =>
          Result.Map(
            "type" -> "fluid",
            "mode" -> x.rsMode.toString,
            "source" -> x.slot.id,
            "valid_sources" -> Result(x.slotsDef.slotMap.keys.toList),
            "average" -> x.avg
          )
        case Some(x: OutputConfigPower) =>
          Result.Map(
            "type" -> "power",
            "mode" -> x.rsMode.toString,
            "unit" -> x.unit,
            "average" -> x.avg
          )
        case Some(x: OutputConfigItems) =>
          Result.Map(
            "type" -> "items",
            "mode" -> x.rsMode.toString
          )
        case None => Result.Map("type" -> "unconnected")
        case _ => Result.Map("type" -> "unknown")
      })
    }
  }

  command("setOutputMode") { ctx =>
    val core = getCore(ctx)
    val (oName, mode) = ctx.params(PString, PString)
    val oNum = outputNames.getOrElse(oName.toLowerCase(Locale.US), err("Invalid output name"))
    val newMode = try {
      RSMode.withName(mode.toUpperCase(Locale.US))
    } catch {
      case e: NoSuchElementException => err("Invalid output mode")
    }
    core.outputConfig.get(oNum) match {
      case Some(x: OutputConfigRSControllable) =>
        x.rsMode = newMode
        core.outputConfig.updated()
        true
      case _ => err("Unable to set output state")
    }
  }

  command("setOutputSource") { ctx =>
    val core = getCore(ctx)
    val (oName, mode) = ctx.params(PString, PString)
    val oNum = outputNames.getOrElse(oName.toLowerCase(Locale.US), err("Invalid output name"))
    core.outputConfig.get(oNum) match {
      case Some(x: OutputConfigFluidSlots) =>
        x.slot = x.slotsDef.slotMap.getOrElse(mode.toUpperCase(Locale.US), err("Invalid output source"))
        core.outputConfig.updated()
        true
      case _ => err("Unable to set output source")
    }
  }
}
