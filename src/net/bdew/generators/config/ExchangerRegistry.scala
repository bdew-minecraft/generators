/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.config

import net.bdew.lib.resource.ResourceKind

object ExchangerRegistry {

  object EntryKind extends Enumeration {
    val HEATING = Value("HEATING")
    val COOLING = Value("COOLING")
  }

  case class Entry(in: ResourceKind, out: ResourceKind, inPerHU: Double, outPerHU: Double, direction: EntryKind.Value)

  var map = Map.empty[ResourceKind, Entry]

  def addCooling(in: ResourceKind, out: ResourceKind, inPerHU: Double, outPerHU: Double) =
    map += (in -> Entry(in, out, inPerHU, outPerHU, EntryKind.COOLING))

  def addHeating(in: ResourceKind, out: ResourceKind, inPerHU: Double, outPerHU: Double) =
    map += (in -> Entry(in, out, inPerHU, outPerHU, EntryKind.HEATING))

  def remove(in: ResourceKind) = map -= in

  def isValidHeater(r: ResourceKind) = map.get(r).exists(_.direction == EntryKind.HEATING)
  def isValidCooler(r: ResourceKind) = map.get(r).exists(_.direction == EntryKind.COOLING)
  def isValidInput(r: ResourceKind) = map.get(r).isDefined

  def getHeating(r: ResourceKind) = map.get(r).filter(_.direction == EntryKind.HEATING)
  def getCooling(r: ResourceKind) = map.get(r).filter(_.direction == EntryKind.COOLING)
}
