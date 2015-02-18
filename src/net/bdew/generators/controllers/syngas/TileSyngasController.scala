/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.controllers.syngas

import net.bdew.generators.config.{Blocks, CarbonValueRegistry}
import net.bdew.generators.{Generators, GeneratorsResourceProvider}
import net.bdew.lib.Misc
import net.bdew.lib.data._
import net.bdew.lib.data.base.UpdateKind
import net.bdew.lib.multiblock.interact.{CIFluidInput, CIOutputFaces}
import net.bdew.lib.multiblock.tile.TileControllerGui
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids._

class TileSyngasController extends TileControllerGui with CIFluidInput with CIOutputFaces {
  override val cfg = MachineSyngas
  override val resources = GeneratorsResourceProvider
  override lazy val maxOutputs = 6

  val inventory = new DataSlotInventory("inv", this, 4) {
    override def isItemValidForSlot(slot: Int, stack: ItemStack) = CarbonValueRegistry.getValue(stack) > 0
  }

  val carbonBuffer = DataSlotDouble("carbon", this).setUpdate(UpdateKind.GUI, UpdateKind.SAVE)
  val steamBuffer = DataSlotDouble("steam", this).setUpdate(UpdateKind.GUI, UpdateKind.SAVE)
  val heat = DataSlotDouble("heat", this).setUpdate(UpdateKind.GUI, UpdateKind.SAVE)
  val waterTank = DataSlotTankRestricted("waterTank", this, cfg.internalTankCapacity, FluidRegistry.getFluidID("water"))
  val syngasTank = DataSlotTankRestricted("syngasTank", this, cfg.internalTankCapacity, Blocks.syngasFluid.getID)

  val heatingChambers = DataSlotInt("heatingChambers", this)
  val mixingChambers = DataSlotInt("mixingChambers", this)

  def doUpdate(): Unit = {
    // Consume carbon to add heat
    if (heat < cfg.maxHeat && carbonBuffer > 0) {
      val addHeat = Misc.min(
        cfg.maxHeat - heat,
        carbonBuffer / cfg.carbonPerHeat,
        mixingChambers * cfg.mixingChamberThroughput
      )
      heat += addHeat
      carbonBuffer -= addHeat * cfg.carbonPerHeat
    }

    // Consume heat to make steam
    if (heat > cfg.workHeat && waterTank.getFluidAmount > 0 && steamBuffer < cfg.internalTankCapacity) {
      val addSteam = Misc.min(
        (cfg.workHeat - heat) * cfg.steamPerHeat,
        waterTank.getFluidAmount * cfg.waterSteamRatio,
        cfg.internalTankCapacity - steamBuffer,
        heatingChambers * cfg.heatingChamberThroughput

      )
      heat -= addSteam / cfg.steamPerHeat
      steamBuffer += addSteam
      waterTank.drain((addSteam / cfg.waterSteamRatio).ceil.toInt, true)
    }

    // Consume steam and carbon to make syngas
    if (steamBuffer > 0 && carbonBuffer > 0 && syngasTank.getFluidAmount < syngasTank.getCapacity) {
      val addSyngas = Misc.min[Double](
        carbonBuffer / cfg.carbonValuePerMBSyngas,
        steamBuffer / cfg.steamPerMBSyngas,
        syngasTank.getCapacity - syngasTank.getFluidAmount,
        cfg.mixingChamberThroughput * mixingChambers
      ).floor.toInt
      carbonBuffer -= addSyngas * cfg.carbonValuePerMBSyngas
      steamBuffer -= addSyngas * cfg.steamPerMBSyngas
      syngasTank.fill(addSyngas, true)
    }

    if (heat > 0) {
      heat := Misc.min(heat - cfg.heatingChamberHeating * cfg.heatLossMultiplier * heatingChambers, 0D)
    }

    // Consume fuel to add carbon
    for {
      slot <- 0 until inventory.size
      stack <- Option(inventory.getStackInSlot(slot))
      cValue <- CarbonValueRegistry.getValueOpt(stack)
      if cfg.internalTankCapacity - carbonBuffer > cValue
    } {
      inventory.decrStackSize(slot, 1)
      carbonBuffer += cValue
    }
  }

  serverTick.listen(doUpdate)

  override def openGui(player: EntityPlayer) = player.openGui(Generators, cfg.guiId, worldObj, xCoord, yCoord, zCoord)

  def inputFluid(resource: FluidStack, doFill: Boolean): Int =
    if (canInputFluid(resource.getFluid)) waterTank.fill(resource, doFill) else 0

  def canInputFluid(fluid: Fluid) = fluid != null && fluid.getName == "water"
  def getTankInfo = Array(waterTank.getInfo, syngasTank.getInfo)

  def onModulesChanged(): Unit = {
    heatingChambers := getNumOfModules("HeatingChamber")
    mixingChambers := getNumOfModules("MixingChamber")
  }
}
