/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.gui

import net.bdew.lib.Misc
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.{WidgetButtonIcon, WidgetSubcontainer}
import net.bdew.lib.multiblock.data.OutputConfigSlots
import net.bdew.lib.multiblock.interact.CIOutputFaces
import net.bdew.lib.multiblock.network.{MsgOutputCfgSlot, NetHandler}

class WidgetSlotConfig(te: CIOutputFaces, output: Int, p: Point) extends WidgetSubcontainer(new Rect(p, 16, 16)) {
  def cfg = te.outputConfig(output).asInstanceOf[OutputConfigSlots]

  val bt = add(new WidgetButtonIcon(Point(0, 0), clicked, te.resources.btBase, te.resources.btHover))

  override def draw(mouse: Point) {
    bt.icon = cfg.slot.texture
    bt.hover = Misc.toLocal(cfg.slot.name)
    super.draw(mouse)
  }

  def clicked(b: WidgetButtonIcon) {
    NetHandler.sendToServer(MsgOutputCfgSlot(output, cfg.slot.next.id))
  }
}
