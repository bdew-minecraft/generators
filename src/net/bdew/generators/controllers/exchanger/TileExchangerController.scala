/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.controllers.exchanger

import net.bdew.generators.config.{ExchangerRegistry, Modules}
import net.bdew.generators.control.{CIControl, ControlActions}
import net.bdew.generators.sensor.Sensors
import net.bdew.generators.{Generators, GeneratorsResourceProvider}
import net.bdew.lib.Misc
import net.bdew.lib.data.base.UpdateKind
import net.bdew.lib.data.{DataSlotDouble, DataSlotMovingAverage}
import net.bdew.lib.multiblock.interact.{CIFluidInput, CIFluidOutputSelect, CIItemOutput, CIOutputFaces}
import net.bdew.lib.multiblock.tile.TileControllerGui
import net.bdew.lib.resource._
import net.bdew.lib.sensors.multiblock.CIRedstoneSensors
import net.bdew.lib.tile.inventory.MultipleInventoryAdapter
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fluids.capability.IFluidHandler

class TileExchangerController extends TileControllerGui with CIFluidInput with CIOutputFaces with CIFluidOutputSelect with CIItemOutput with CIRedstoneSensors with CIControl {
  val cfg = MachineExchanger

  val resources = GeneratorsResourceProvider

  val heaterIn = new DataSlotResource("heaterIn", this, cfg.internalTankCapacity, canDrainExternal = false, canAccept = ExchangerRegistry.isValidHeater)
  val coolerIn = new DataSlotResource("coolerIn", this, cfg.internalTankCapacity, canDrainExternal = false, canAccept = ExchangerRegistry.isValidCooler)
  val heaterOut = new DataSlotResource("heaterOut", this, cfg.internalTankCapacity, canFillExternal = false)
  val coolerOut = new DataSlotResource("coolerOut", this, cfg.internalTankCapacity, canFillExternal = false)
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
    if (getControlStateWithDefault(ControlActions.exchangeHeat, true) && heat > cfg.startHeating && coolerIn.resource.isDefined) {
      val transfer = Misc.clamp(heat.value - cfg.startHeating, 0D, maxHeatTransfer.value)
      for {
        cooler <- coolerIn.resource
        rec <- ExchangerRegistry.getCooling(cooler.kind)
      } {
        val maxFill = coolerOut.fillInternal(Resource(rec.out, transfer * rec.outPerHU), false, false)
        val toDrain = Misc.min(maxFill / rec.outPerHU * rec.inPerHU, transfer * rec.inPerHU, cooler.amount)
        if (toDrain > 0) {
          coolerIn.drainInternal(toDrain, false, true)
          coolerOut.fillInternal(Resource(rec.out, toDrain / rec.inPerHU * rec.outPerHU), false, true)
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
    if (getControlStateWithDefault(ControlActions.exchangeHeat, true) && heat < cfg.maxHeat && heaterIn.resource.isDefined) {
      val transfer = Misc.clamp(cfg.maxHeat - heat.value, 0D, maxHeatTransfer.value)
      for {
        heater <- heaterIn.resource
        rec <- ExchangerRegistry.getHeating(heater.kind)
      } {
        val maxFill = heaterOut.fillInternal(Resource(rec.out, transfer * rec.outPerHU), false, false)
        val toDrain = Misc.min(maxFill / rec.outPerHU * rec.inPerHU, transfer * rec.inPerHU, heater.amount)
        if (toDrain > 0) {
          heaterIn.drainInternal(toDrain, false, true)
          heaterOut.fillInternal(Resource(rec.out, toDrain / rec.inPerHU * rec.outPerHU), false, true)
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

  override def openGui(player: EntityPlayer) = player.openGui(Generators, cfg.guiId, worldObj, pos.getX, pos.getY, pos.getZ)

  override def onModulesChanged() {
    maxHeatTransfer := getNumOfModules("HeatExchanger") * Modules.HeatExchanger.heatTransfer
    super.onModulesChanged()
  }

  override val outputSlotsDef = OutputSlotsExchanger

  override def getInputTanks: List[IFluidHandler] = List(coolerIn.fluidHandler, heaterIn.fluidHandler)

  override def getOutputTanksForSlot(slot: outputSlotsDef.Slot): List[IFluidHandler] = {
    slot match {
      case OutputSlotsExchanger.BOTH => List(coolerOut.fluidHandler, heaterOut.fluidHandler)
      case OutputSlotsExchanger.COLD => List(heaterOut.fluidHandler)
      case OutputSlotsExchanger.HOT => List(coolerOut.fluidHandler)
    }
  }

  override def getItemOutputInventory = outInventory

  override def canOutputItemFromSlot(slot: Int) = true

  override def redstoneSensorsType = Sensors.exchangerSensors
  override def redstoneSensorSystem = Sensors

  override def availableControlActions = List(ControlActions.disabled, ControlActions.exchangeHeat)
}
