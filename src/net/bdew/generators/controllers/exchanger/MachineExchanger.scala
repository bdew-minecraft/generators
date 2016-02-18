/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.controllers.exchanger

import net.bdew.lib.gui.GuiProvider
import net.bdew.lib.machine.Machine
import net.bdew.lib.multiblock.MachineCore
import net.bdew.lib.multiblock.data.{OutputConfigFluidSlots, OutputConfigManager}
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

object MachineExchanger extends Machine("HeatExchangerController", BlockExchangerController) with MachineCore with GuiProvider {
  def guiId: Int = 2
  type TEClass = TileExchangerController

  lazy val internalTankCapacity = tuning.getInt("InternalTankCapacity")
  lazy val maxHeat = tuning.getDouble("MaxHeat")
  lazy val startHeating = tuning.getDouble("StartHeating")
  lazy val heatDecay = tuning.getDouble("HeatDecay")

  OutputConfigManager.register("fluidslots_exchanger", () => new OutputConfigFluidSlots(OutputSlotsExchanger))

  @SideOnly(Side.CLIENT)
  def getGui(te: TileExchangerController, player: EntityPlayer) = new GuiExchanger(te, player)
  def getContainer(te: TileExchangerController, player: EntityPlayer) = new ContainerExchanger(te, player)
}
