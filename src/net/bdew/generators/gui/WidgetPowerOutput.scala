/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.gui

import net.bdew.lib.DecFormat
import net.bdew.lib.gui.widgets.WidgetDynLabel
import net.bdew.lib.gui.{Color, Point}
import net.bdew.lib.multiblock.data._
import net.bdew.lib.multiblock.gui.WidgetRSConfig
import net.bdew.lib.multiblock.interact.CIOutputFaces

class WidgetPowerOutput(te: CIOutputFaces, output: Int) extends WidgetOutputDisplay {
  def cfg = te.outputConfig(output).asInstanceOf[OutputConfigPower]
  add(new WidgetDynLabel("%s %s/t".format(DecFormat.round(cfg.avg), cfg.unit), 1, 5, Color.darkgray))
  add(new WidgetRSConfig(te, output, Point(73, 1)))
}
