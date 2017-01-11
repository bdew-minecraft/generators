/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.gui

import net.bdew.generators.config.Config
import net.bdew.lib.DecFormat
import net.bdew.lib.gui._
import net.bdew.lib.power.{DataSlotPower, WidgetPowerGauge}

import scala.collection.mutable

class WidgetPowerGaugeCustom(rect: Rect, texture: Texture, dSlot: DataSlotPower) extends WidgetPowerGauge(rect, texture, dSlot) {
  override def handleTooltip(p: Point, tip: mutable.MutableList[String]) =
    tip += "%s/%s %s".format(DecFormat.round(dSlot.stored * Config.powerShowMultiplier), DecFormat.round(dSlot.capacity * Config.powerShowMultiplier), Config.powerShowUnits)
}
