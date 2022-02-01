package net.bdew.generators.gui

import net.bdew.generators.Generators
import net.bdew.generators.registries.Containers
import net.bdew.lib.container.NoInvContainer
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.WidgetLabel
import net.bdew.lib.multiblock.interact.CIOutputFaces
import net.bdew.lib.{Client, Text}
import net.minecraft.world.entity.player.{Inventory, Player}

class ContainerOutputConfig extends NoInvContainer(Containers.outputConfig.get(), -1) {
  override def stillValid(player: Player): Boolean = true
}

class GuiOutputConfig(parent: BaseScreen[_], te: CIOutputFaces, pi: Inventory) extends BaseScreen(new ContainerOutputConfig, pi, Text.translate("advgenerators.gui.output.title")) {
  override val background: Sprite = Texture(Generators.ModId, "textures/gui/outputs.png", Rect(0, 0, 124, 138))

  override def init(): Unit = {
    initGui(124, 138)
    widgets.add(new WidgetOutputs(Point(7, 18), te, 6))
    widgets.add(new WidgetLabel(Text.translate("advgenerators.gui.output.title"), 8, 6, Color.darkGray))
  }

  override def onClose(): Unit = {
    Client.minecraft.setScreen(parent)
  }
}
