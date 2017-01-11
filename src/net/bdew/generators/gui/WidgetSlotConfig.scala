/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.gui

import net.bdew.lib.Misc
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.{WidgetButtonIcon, WidgetSubContainer}
import net.bdew.lib.multiblock.data.OutputConfigSlots
import net.bdew.lib.multiblock.interact.CIOutputFaces
import net.bdew.lib.multiblock.network.{MsgOutputCfgSlot, NetHandler}

import scala.collection.mutable

class WidgetSlotConfig(te: CIOutputFaces, output: Int, p: Point) extends WidgetSubContainer(new Rect(p, 16, 16)) {
  def cfg = te.outputConfig(output).asInstanceOf[OutputConfigSlots]
  val bt = add(new WidgetButtonIcon(Point(0, 0), clicked, te.resources.btBase, te.resources.btHover))

  override def handleTooltip(p: Point, tip: mutable.MutableList[String]): Unit =
    if (cfg.slotsDef.slotMap.size > 1)
      super.handleTooltip(p, tip)

  override def draw(mouse: Point) {
    if (cfg.slotsDef.slotMap.size > 1) {
      bt.icon = cfg.slot.texture
      bt.hover = Misc.toLocal(cfg.slot.name)
      super.draw(mouse)
    }
  }

  def clicked(b: WidgetButtonIcon) {
    if (cfg.slotsDef.slotMap.size > 1)
      NetHandler.sendToServer(MsgOutputCfgSlot(output, cfg.slot.next.id))
  }
}
