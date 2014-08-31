/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.blocks.turbineController

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.bdew.generators.Generators
import net.bdew.generators.compat.{BCCompat, PowerProxy}
import net.bdew.lib.gui.GuiProvider
import net.bdew.lib.machine.Machine
import net.bdew.lib.multiblock.MachineCore
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fluids.{Fluid, FluidRegistry}

object MachineTurbine extends Machine("TurbineController", BlockTurbineController) with MachineCore with GuiProvider {
  def guiId: Int = 1
  type TEClass = TileTurbineController

  var finalFuelValues = Map.empty[Fluid, Float]

  lazy val fuelValues = tuning.getSection("FuelValues")
  lazy val mjPerTickPerTurbine = tuning.getFloat("MJPerTickPerTurbine")
  lazy val fuelConsumptionMultiplier = tuning.getFloat("FuelConsumptionMultiplier")
  lazy val internalPowerCapacity = tuning.getInt("InternalPowerCapacity")
  lazy val internalFuelCapacity = tuning.getInt("InternalFuelCapacity")
  lazy val minFuelValue = tuning.getFloat("MinimumFuelValue")

  def getFuelValue(fluid: Fluid) = finalFuelValues.getOrElse(fluid, 0F)

  def loadFuelValues() {
    if (tuning.getBoolean("ImportCombustionEngineFuels") && PowerProxy.haveBCfuel)
      finalFuelValues ++= BCCompat.getCombustionEngineFuels

    finalFuelValues ++= (fuelValues.keys
      filter { id =>
      if (FluidRegistry.isFluidRegistered(id)) {
        true
      } else {
        Generators.logWarn("Fuel not found: %s", id)
        false
      }
    } map FluidRegistry.getFluid
      map (f => f -> fuelValues.getFloat(f.getName))
      ).toMap

    finalFuelValues = finalFuelValues.filter(_._2 > minFuelValue) // remove disabled fuels

    Generators.logInfo("Turbine fuels:")
    for ((fuel, value) <- finalFuelValues) {
      Generators.logInfo(" * %s: %.0f MJ/MB".format(fuel.getName, value))
    }
  }

  @SideOnly(Side.CLIENT)
  def getGui(te: TileTurbineController, player: EntityPlayer) = new GuiTurbine(te, player)
  def getContainer(te: TileTurbineController, player: EntityPlayer) = new ContainerTurbine(te, player)
}
