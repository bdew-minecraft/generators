package net.bdew.generators.modules.control

import net.bdew.generators.Generators
import net.bdew.generators.control.{WidgetControlAction, WidgetControlMode}
import net.bdew.lib.Text
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.WidgetLabel
import net.minecraft.world.entity.player.Inventory

class GuiControl(container: ContainerControl, playerInv: Inventory)
  extends BaseScreen(container, playerInv, Text.translate("advgenerators.gui.control.title")) {
  val te: TileControl = container.te
  val background: Sprite = Texture(Generators.ModId, "textures/gui/control.png", Rect(0, 0, 176, 175))

  override def init(): Unit = {
    initGui(176, 175)

    widgets.add(new WidgetControlAction(Point(98, 38), te.action))
    widgets.add(new WidgetControlMode(Point(62, 38), te.mode))

    widgets.add(new WidgetLabel(title, 8, 6, Color.darkGray))
    widgets.add(new WidgetLabel(Text.translate("container.inventory"), 8, rect.h - 96 + 3, Color.darkGray))
  }
}

