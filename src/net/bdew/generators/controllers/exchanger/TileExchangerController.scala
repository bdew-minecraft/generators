/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.controllers.exchanger

import net.bdew.generators.config.{ExchangerRegistry, Modules}
import net.bdew.generators.gui.{DataSlotMovingAverage, DataSlotResourceKindOption}
import net.bdew.generators.{Generators, GeneratorsResourceProvider}
import net.bdew.lib.Misc
import net.bdew.lib.data.DataSlotDouble
import net.bdew.lib.data.base.UpdateKind
import net.bdew.lib.multiblock.interact.{CIFluidInput, CIFluidOutputSelect, CIItemOutput, CIOutputFaces}
import net.bdew.lib.multiblock.tile.TileControllerGui
import net.bdew.lib.resource._
import net.bdew.lib.tile.inventory.MultipleInventoryAdapter
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fluids.{Fluid, FluidStack}

class TileExchangerController extends TileControllerGui with CIFluidInput with CIOutputFaces with CIFluidOutputSelect with CIItemOutput {
  val cfg = MachineExchanger

  val resources = GeneratorsResourceProvider

  val heaterIn = new DataSlotResource("heaterIn", this, cfg.internalTankCapacity)
  val coolerIn = new DataSlotResource("coolerIn", this, cfg.internalTankCapacity)
  val heaterOut = new DataSlotResource("heaterOut", this, cfg.internalTankCapacity)
  val coolerOut = new DataSlotResource("coolerOut", this, cfg.internalTankCapacity)
  val heat = new DataSlotDouble("heat", this).setUpdate(UpdateKind.SAVE, UpdateKind.GUI)
  val maxHeatTransfer = new DataSlotDouble("maxHeatTransfer", this).setUpdate(UpdateKind.SAVE, UpdateKind.GUI)

  val inputRate = DataSlotMovingAverage("inputRate", this, 20)
  val outputRate = DataSlotMovingAverage("outputRate", this, 20)
  val lastInput = DataSlotResourceKindOption("lastInputRes", this)
  val lastOutput = DataSlotResourceKindOption("lastOutputRes", this)
  val heatLoss = DataSlotMovingAverage("heatLoss", this, 20)

  var outInventory = new MultipleInventoryAdapter(
    ResourceInventoryOutput(heaterOut), ResourceInventoryOutput(coolerOut)
  )

  lazy val maxOutputs = 6

  def doUpdate() {
    var tickInput = 0D
    var tickOutput = 0D

    // first use heat
    if (heat > cfg.startHeating && coolerIn.resource.isDefined) {
      val transfer = Misc.clamp(heat.value - cfg.startHeating, 0D, maxHeatTransfer.value)
      for {
        cooler <- coolerIn.resource
        rec <- ExchangerRegistry.getCooling(cooler.kind)
      } {
        val maxFill = coolerOut.rawFill(Resource(rec.out, transfer * rec.outPerHU), false, false)
        val toDrain = Misc.min(maxFill / rec.outPerHU * rec.inPerHU, transfer * rec.inPerHU, cooler.amount)
        if (toDrain > 0) {
          coolerIn.rawDrain(toDrain, false, true)
          coolerOut.rawFill(Resource(rec.out, toDrain / rec.inPerHU * rec.outPerHU), false, true)
          tickOutput = toDrain / rec.inPerHU * rec.outPerHU
          if (!lastOutput.contains(rec.out)) {
            lastOutput.set(rec.out)
            outputRate.values.clear()
          }
          heat -= toDrain / rec.inPerHU
        }
      }
    }

    // head decay is applied now to penalize overheated small exchanger and inactive exchangers in general
    if (heat > 0) {
      heatLoss.update(heat * cfg.heatDecay)
      heat := heat * (1 - cfg.heatDecay)
    } else {
      heatLoss.update(0)
    }

    // and finally restore heat
    if (heat < cfg.maxHeat && heaterIn.resource.isDefined) {
      val transfer = Misc.clamp(cfg.maxHeat - heat.value, 0D, maxHeatTransfer.value)
      for {
        heater <- heaterIn.resource
        rec <- ExchangerRegistry.getHeating(heater.kind)
      } {
        val maxFill = heaterOut.rawFill(Resource(rec.out, transfer * rec.outPerHU), false, false)
        val toDrain = Misc.min(maxFill / rec.outPerHU * rec.inPerHU, transfer * rec.inPerHU, heater.amount)
        if (toDrain > 0) {
          heaterIn.rawDrain(toDrain, false, true)
          heaterOut.rawFill(Resource(rec.out, toDrain / rec.inPerHU * rec.outPerHU), false, true)
          tickInput = toDrain
          if (!lastInput.contains(rec.in)) {
            lastInput.set(rec.in)
            inputRate.values.clear()
          }
          heat += toDrain / rec.inPerHU
        }
      }
    }

    inputRate.update(tickInput)
    outputRate.update(tickOutput)
  }

  serverTick.listen(doUpdate)

  override def openGui(player: EntityPlayer) = player.openGui(Generators, cfg.guiId, worldObj, xCoord, yCoord, zCoord)

  override def inputFluid(resource: FluidStack, doFill: Boolean): Int = {
    val res = Resource.from(resource)
    if (ExchangerRegistry.isValidCooler(res.kind))
      coolerIn.fillFluid(resource, doFill)
    else if (ExchangerRegistry.isValidHeater(res.kind))
      heaterIn.fillFluid(resource, doFill)
    else 0
  }

  override def canInputFluid(fluid: Fluid) = ExchangerRegistry.isValidInput(FluidResource(fluid))

  override def getTankInfo = Array(heaterIn.getTankInfo, coolerIn.getTankInfo, heaterOut.getTankInfo, coolerOut.getTankInfo)

  def onModulesChanged() {
    maxHeatTransfer := getNumOfModules("HeatExchanger") * Modules.HeatExchanger.heatTransfer
  }

  override val outputSlotsDef = OutputSlotsExchanger

  def getTanks(slot: OutputSlotsExchanger.Slot) = slot match {
    case OutputSlotsExchanger.BOTH => Array(coolerOut, heaterOut)
    case OutputSlotsExchanger.COLD => Array(heaterOut)
    case OutputSlotsExchanger.HOT => Array(coolerOut)
  }

  override def outputFluid(slot: OutputSlotsExchanger.Slot, resource: FluidStack, doDrain: Boolean): FluidStack = {
    for {
      tank <- getTanks(slot)
      res <- tank.resource if res.kind == FluidResource(resource.getFluid)
      fs <- Option(tank.drainFluid(resource.amount, doDrain))
    } {
      return fs
    }
    return null
  }

  override def outputFluid(slot: OutputSlotsExchanger.Slot, amount: Int, doDrain: Boolean): FluidStack = {
    for {
      tank <- getTanks(slot)
      fs <- Option(tank.drainFluid(amount, doDrain))
    } {
      return fs
    }
    return null
  }

  override def canOutputFluid(slot: OutputSlotsExchanger.Slot, fluid: Fluid) =
    getTanks(slot).exists(t => t.resource.contains(FluidResource(fluid)))

  override def getItemOutputInventory = outInventory

  override def canOutputItemFromSlot(slot: Int) = true
}
