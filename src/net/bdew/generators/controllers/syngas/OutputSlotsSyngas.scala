/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.controllers.syngas

import net.bdew.lib.multiblock.data.SlotSet
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

object OutputSlotsSyngas extends SlotSet("fluidslots_syngas") {
  val SYNGAS = Slot("SYNGAS", "advgenerators.syngas.output.syngas")
  override def default = SYNGAS
  override val order = Map(SYNGAS -> SYNGAS)

  @SideOnly(Side.CLIENT)
  override def textures = Map.empty
}
