package net.bdew.generators.control

import com.mojang.blaze3d.vertex.PoseStack
import net.bdew.lib.Text
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.Widget
import net.minecraft.network.chat.Component

import scala.collection.mutable.ArrayBuffer

class WidgetControlMode(val p: Point, state: => Boolean) extends Widget {
  lazy val rsOn: IconWrapper = Texture.block("minecraft:block/redstone_torch")
  lazy val rsOff: IconWrapper = Texture.block("minecraft:block/redstone_torch_off")

  override val rect = new Rect(p, 16, 16)

  override def handleTooltip(p: Point, tip: ArrayBuffer[Component]): Unit =
    if (state)
      tip += Text.translate("advgenerators.control.mode.high")
    else
      tip += Text.translate("advgenerators.control.mode.low")

  override def draw(m: PoseStack, mouse: Point, partial: Float): Unit =
    if (state)
      parent.drawTexture(m, rect, rsOn)
    else
      parent.drawTexture(m, rect, rsOff)
}

class WidgetControlAction(val p: Point, action: => ControlAction) extends Widget {
  override val rect = new Rect(p, 16, 16)

  override def handleTooltip(p: Point, tip: ArrayBuffer[Component]): Unit =
    tip += Text.translate("advgenerators.control.action." + action.uid)

  override def draw(m: PoseStack, mouse: Point, partial: Float): Unit =
    parent.drawTexture(m, rect, action.texture)
}
