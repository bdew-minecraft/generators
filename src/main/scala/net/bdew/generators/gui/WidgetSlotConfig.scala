package net.bdew.generators.gui

import com.mojang.blaze3d.matrix.MatrixStack
import net.bdew.lib.Text
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.{WidgetButtonIcon, WidgetSubContainer}
import net.bdew.lib.multiblock.data.OutputConfigSlots
import net.bdew.lib.multiblock.interact.CIOutputFaces
import net.bdew.lib.multiblock.network.{MsgOutputCfgSlot, MultiblockNetHandler}
import net.minecraft.util.text.ITextComponent

import scala.collection.mutable.ArrayBuffer

class WidgetSlotConfig(te: CIOutputFaces, output: Int, p: Point) extends WidgetSubContainer(new Rect(p, 16, 16)) {
  def cfg: OutputConfigSlots = te.outputConfig(output).asInstanceOf[OutputConfigSlots]

  val bt: WidgetButtonIcon = add(new WidgetButtonIcon(Point(0, 0), clicked, te.resources.btBase, te.resources.btHover) {
    override def icon: Texture = cfg.slot.texture
    override def hover: ITextComponent = Text.translate(cfg.slot.name)
  })

  override def handleTooltip(p: Point, tip: ArrayBuffer[ITextComponent]): Unit =
    if (cfg.slotsDef.slotMap.size > 1)
      super.handleTooltip(p, tip)

  override def draw(m: MatrixStack, mouse: Point, partial: Float): Unit = {
    if (cfg.slotsDef.slotMap.size > 1) {
      super.draw(m: MatrixStack, mouse, partial)
    }
  }

  override def drawBackground(matrix: MatrixStack, mouse: Point): Unit = {
    if (cfg.slotsDef.slotMap.size > 1) {
      super.drawBackground(matrix, mouse)
    }
  }

  def clicked(b: WidgetButtonIcon): Unit = {
    if (cfg.slotsDef.slotMap.size > 1)
      MultiblockNetHandler.sendToServer(MsgOutputCfgSlot(output, cfg.slot.next.id))
  }
}
