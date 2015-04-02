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
import net.bdew.lib.multiblock.interact.{CIFluidInput, CIFluidOutputSelect, CIItemInput, CIOutputFaces}
import net.bdew.lib.multiblock.tile.TileControllerGui
import net.bdew.lib.tile.TankEmulator
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids._

class TileSyngasController extends TileControllerGui with CIFluidInput with CIItemInput with CIOutputFaces with CIFluidOutputSelect {
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

  lazy val steamTank = TankEmulator(Blocks.steamFluid, steamBuffer, cfg.internalTankCapacity)

  val heatingChambers = DataSlotInt("heatingChambers", this).setUpdate(UpdateKind.SAVE, UpdateKind.GUI)
  val mixingChambers = DataSlotInt("mixingChambers", this)

  val avgCarbonUsed = DataSlotMovingAverage("carbonUsed", this, 20)
  val avgSyngasProduced = DataSlotMovingAverage("syngasProduced", this, 20)
  val avgHeatDelta = DataSlotMovingAverage("heatDelta", this, 20)

  def doUpdate(): Unit = {
    var carbonUsed = 0D
    var syngasProduced = 0D
    var heatDelta = 0D

    // Consume carbon to add heat
    if (heat < cfg.maxHeat && carbonBuffer > 0 && heatingChambers > 0 && waterTank.getFluidAmount > 0) {
      val addHeat = Misc.min(
        cfg.maxHeat - heat,
        carbonBuffer / cfg.carbonPerHeat,
        heatingChambers * cfg.heatingChamberHeating
      )
      heat += addHeat
      carbonBuffer -= addHeat * cfg.carbonPerHeat
      carbonUsed += addHeat * cfg.carbonPerHeat
      heatDelta += addHeat
    }

    // Consume water to make steam
    if (heat > cfg.workHeat && waterTank.getFluidAmount > 0 && steamBuffer < cfg.internalTankCapacity && heatingChambers > 0) {
      val addSteam = Misc.min(
        waterTank.getFluidAmount * cfg.waterSteamRatio,
        cfg.internalTankCapacity - steamBuffer,
        heatingChambers * cfg.heatingChamberThroughput * (heat / cfg.maxHeat)
      )
      steamBuffer += addSteam
      waterTank.drain((addSteam / cfg.waterSteamRatio).ceil.toInt, true)
    }

    // Consume steam and carbon to make syngas
    if (steamBuffer > 0 && carbonBuffer > 0 && syngasTank.getFluidAmount < syngasTank.getCapacity) {
      val addSyngas = Misc.min[Double](
        carbonBuffer / cfg.carbonPerMBSyngas,
        steamBuffer / cfg.steamPerMBSyngas,
        syngasTank.getCapacity - syngasTank.getFluidAmount,
        cfg.mixingChamberThroughput * mixingChambers
      ).floor.toInt
      carbonBuffer -= addSyngas * cfg.carbonPerMBSyngas
      steamBuffer -= addSyngas * cfg.steamPerMBSyngas
      syngasTank.fill(addSyngas, true)
      syngasProduced += addSyngas
      carbonUsed += addSyngas * cfg.carbonPerMBSyngas
    }

    if (heat > 0) {
      val heatLoss = Misc.min(heat.value, heatingChambers * cfg.heatingChamberLoss)
      heat -= heatLoss
      heatDelta -= heatLoss
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

    avgCarbonUsed.update(carbonUsed)
    avgSyngasProduced.update(syngasProduced)
    avgHeatDelta.update(heatDelta)
  }

  serverTick.listen(doUpdate)

  override def openGui(player: EntityPlayer) = player.openGui(Generators, cfg.guiId, worldObj, xCoord, yCoord, zCoord)

  def inputFluid(resource: FluidStack, doFill: Boolean): Int =
    if (canInputFluid(resource.getFluid)) {
      if (resource.getFluid == FluidRegistry.WATER)
        waterTank.fill(resource, doFill)
      else if (resource.getFluid == Blocks.steamFluid)
        steamTank.fill(resource, doFill)
      else 0
    } else 0

  def canInputFluid(fluid: Fluid) = fluid != null && (fluid == FluidRegistry.WATER || fluid == Blocks.steamFluid)
  def getTankInfo = Array(waterTank.getInfo, syngasTank.getInfo)

  def onModulesChanged(): Unit = {
    heatingChambers := getNumOfModules("HeatingChamber")
    mixingChambers := getNumOfModules("MixingChamber")
  }

  override def getItemInputInventory = inventory
  override def canInputItemToSlot(slot: Int) = true

  override val outputSlotsDef = OutputSlotsSyngas
  override def outputFluid(slot: outputSlotsDef.Slot, amount: Int, doDrain: Boolean) = syngasTank.drain(amount, doDrain)
  override def canOutputFluid(slot: outputSlotsDef.Slot, fluid: Fluid) = fluid == Blocks.syngasFluid
  override def outputFluid(slot: outputSlotsDef.Slot, resource: FluidStack, doDrain: Boolean) =
    if (resource.getFluid == Blocks.syngasFluid)
      syngasTank.drain(resource.amount, doDrain)
    else
      null

}
