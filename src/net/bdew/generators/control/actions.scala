/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.control

case class ControlAction(uid: String, iconName: String) extends Icons.Loader {
  ControlActions.registry += uid -> this
}

object ControlActions {
  var registry = Map.empty[String, ControlAction]

  val disabled = ControlAction("disabled", "disabled")
  val useSteam = ControlAction("useSteam", "steam")
  val useFuel = ControlAction("useFuel", "fuel")
  val generateEnergy = ControlAction("generateEnergy", "energy")
}