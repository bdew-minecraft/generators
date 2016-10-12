/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.dataport

import java.util.Locale

import net.bdew.generators.control.{CIControl, ControlResult}
import net.bdew.lib.computers.{PString, Result}

trait CommandsControl {
  self: BaseCommands[_ <: CIControl] =>

  command("getControls", direct = true) { ctx =>
    Result(
      ctx.tile.controls.value map { case (k, v) =>
        k.uid -> v.toString
      }
    )
  }

  command("setControlStatus") { ctx =>
    val (actionUid, resultName) = ctx.params(PString, PString)
    val action = ctx.tile.controls.keys.find(_.uid == actionUid).getOrElse(err("Unknown control action"))
    val result = try {
      ControlResult.withName(resultName.toUpperCase(Locale.US))
    } catch {
      case e: NoSuchElementException => err("Invalid control result")
    }
    ctx.tile.setControl(action, result)
    true
  }
}
