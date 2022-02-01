package net.bdew.generators.modules.sensor

import net.bdew.generators.Generators
import net.bdew.generators.sensor.Sensors
import net.bdew.lib.Text
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.WidgetLabel
import net.bdew.lib.sensors.widgets.{WidgetSensorParam, WidgetSensorResult, WidgetSensorType}
import net.minecraft.world.entity.player.Inventory

class GuiSensor(container: ContainerSensor, playerInv: Inventory)
  extends BaseScreen(container, playerInv, Text.translate("advgenerators.gui.sensor.title")) {
  val te: TileSensor = container.te
  val background: Sprite = Texture(Generators.ModId, "textures/gui/sensor.png", Rect(0, 0, 176, 175))

  override def init(): Unit = {
    initGui(176, 175)

    widgets.add(new WidgetSensorType(Point(53, 38), te.config.sensor, te.getCore))
    widgets.add(new WidgetSensorParam(Point(71, 38), te.config, te.getCore))
    widgets.add(new WidgetSensorResult(Point(107, 38), te.isSignalOn, Sensors))

    widgets.add(new WidgetLabel(title, 8, 6, Color.darkGray))
    widgets.add(new WidgetLabel(Text.translate("container.inventory"), 8, rect.h - 96 + 3, Color.darkGray))
  }
}

