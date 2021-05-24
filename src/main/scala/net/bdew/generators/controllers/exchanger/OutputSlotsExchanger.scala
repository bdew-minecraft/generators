package net.bdew.generators.controllers.exchanger

import net.bdew.generators.Textures
import net.bdew.lib.gui.Texture
import net.bdew.lib.multiblock.data.SlotSet

object OutputSlotsExchanger extends SlotSet("fluidslots_exchanger") {
  val BOTH: OutputSlotsExchanger.Slot = Slot("BOTH", "advgenerators.exchanger.output.both")
  val COLD: OutputSlotsExchanger.Slot = Slot("COLD", "advgenerators.exchanger.output.cold")
  val HOT: OutputSlotsExchanger.Slot = Slot("HOT", "advgenerators.exchanger.output.hot")
  override def default: OutputSlotsExchanger.Slot = BOTH
  override val order = Map(BOTH -> COLD, COLD -> HOT, HOT -> BOTH)

  override lazy val textures = Map(
    BOTH -> Texture(Textures.sheet, 101, 35, 14, 14),
    COLD -> Texture(Textures.sheet, 133, 35, 14, 14),
    HOT -> Texture(Textures.sheet, 117, 35, 14, 14)
  )
}
