package net.bdew.generators.gui

import net.bdew.lib.Text
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.{WidgetButtonIcon, WidgetSubContainer}
import net.bdew.lib.multiblock.data.OutputConfigSlots
import net.bdew.lib.multiblock.interact.CIOutputFaces
import net.bdew.lib.multiblock.network.{MsgOutputCfgSlot, MultiblockNetHandler}
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component

import scala.collection.mutable.ArrayBuffer

class WidgetSlotConfig(te: CIOutputFaces, output: Int, p: Point) extends WidgetSubContainer(new Rect(p, 16, 16)) {
  def cfg: OutputConfigSlots = te.outputConfig(output).asInstanceOf[OutputConfigSlots]

  val bt: WidgetButtonIcon = add(new WidgetButtonIcon(Point(0, 0), clicked, te.resources.btBase, te.resources.btHover) {
    override def icon: Texture = cfg.slot.texture
    override def hover: Component = Text.translate(cfg.slot.name)
  })

  override def handleTooltip(p: Point, tip: ArrayBuffer[Component]): Unit =
    if (cfg.slotsDef.slotMap.size > 1)
      super.handleTooltip(p, tip)

  override def draw(graphics: GuiGraphics, mouse: Point, partial: Float): Unit = {
    if (cfg.slotsDef.slotMap.size > 1) {
      super.draw(graphics, mouse, partial)
    }
  }

  override def drawBackground(graphics: GuiGraphics, mouse: Point): Unit = {
    if (cfg.slotsDef.slotMap.size > 1) {
      super.drawBackground(graphics, mouse)
    }
  }

  def clicked(b: WidgetButtonIcon): Unit = {
    if (cfg.slotsDef.slotMap.size > 1)
      MultiblockNetHandler.sendToServer(MsgOutputCfgSlot(output, cfg.slot.next.id))
  }
}
