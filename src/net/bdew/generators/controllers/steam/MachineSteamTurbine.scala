/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license
 */

package net.bdew.generators.controllers.steam

import net.bdew.lib.gui.GuiProvider
import net.bdew.lib.machine.Machine
import net.bdew.lib.multiblock.MachineCore
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

object MachineSteamTurbine extends Machine("SteamTurbineController", BlockSteamTurbineController) with MachineCore with GuiProvider {
  def guiId: Int = 3
  type TEClass = TileSteamTurbineController

  lazy val inertiaFunctionMultiplier = tuning.getDouble("InertiaFunctionMultiplier")
  lazy val inertiaFunctionExponent = tuning.getDouble("InertiaFunctionExponent")

  lazy val spinUpMultiplier = tuning.getDouble("SpinUpMultiplier")
  lazy val baseDragMultiplier = tuning.getDouble("BaseDragMultiplier")
  lazy val coilDragMultiplier = tuning.getDouble("CoilDragMultiplier")

  lazy val maxRPM = tuning.getDouble("MaxRPM")

  lazy val steamEnergyDensity = tuning.getDouble("SteamEnergyDensity")

  lazy val internalPowerCapacity = tuning.getInt("InternalPowerCapacity")
  lazy val internalSteamCapacity = tuning.getInt("InternalSteamCapacity")

  @SideOnly(Side.CLIENT)
  def getGui(te: TileSteamTurbineController, player: EntityPlayer) = new GuiSteamTurbine(te, player)
  def getContainer(te: TileSteamTurbineController, player: EntityPlayer) = new ContainerSteamTurbine(te, player)
}
