/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.controllers.turbine

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.bdew.lib.gui.GuiProvider
import net.bdew.lib.machine.Machine
import net.bdew.lib.multiblock.MachineCore
import net.minecraft.entity.player.EntityPlayer

object MachineTurbine extends Machine("TurbineController", BlockTurbineController) with MachineCore with GuiProvider {
  def guiId: Int = 1
  type TEClass = TileTurbineController

  lazy val fuelConsumptionMultiplier = tuning.getFloat("FuelConsumptionMultiplier")
  lazy val internalPowerCapacity = tuning.getInt("InternalPowerCapacity")
  lazy val internalFuelCapacity = tuning.getInt("InternalFuelCapacity")

  @SideOnly(Side.CLIENT)
  def getGui(te: TileTurbineController, player: EntityPlayer) = new GuiTurbine(te, player)
  def getContainer(te: TileTurbineController, player: EntityPlayer) = new ContainerTurbine(te, player)
}
