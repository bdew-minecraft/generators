package net.bdew.generators.controllers.syngas

import net.bdew.lib.gui.Texture
import net.bdew.lib.multiblock.data.SlotSet
import net.minecraftforge.api.distmarker.{Dist, OnlyIn}

object OutputSlotsSyngas extends SlotSet("fluidslots_syngas") {
  val SYNGAS: OutputSlotsSyngas.Slot = Slot("SYNGAS", "advgenerators.syngas.output.syngas")
  override def default: Slot = SYNGAS
  override val order: Map[Slot, Slot] = Map(SYNGAS -> SYNGAS)

  @OnlyIn(Dist.CLIENT)
  override def textures: Map[Slot, Texture] = Map.empty
}
