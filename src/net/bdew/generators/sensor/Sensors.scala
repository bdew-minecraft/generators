/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.sensor

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.bdew.generators.controllers.exchanger.{MachineExchanger, TileExchangerController}
import net.bdew.generators.controllers.steam.{MachineSteamTurbine, TileSteamTurbineController}
import net.bdew.generators.controllers.syngas.{MachineSyngas, TileSyngasController}
import net.bdew.generators.controllers.turbine.TileTurbineController
import net.bdew.generators.sensor.data._
import net.bdew.lib.DecFormat
import net.bdew.lib.sensors.RedstoneSensors
import net.minecraft.tileentity.TileEntity

object Sensors extends RedstoneSensors[TileEntity] {
  @SideOnly(Side.CLIENT)
  override def disabledTexture = Icons.disabled
  override def localizationPrefix = "advgenerators.sensor"

  val fuelTurbineSensors = List(
    DisabledSensor,
    SensorPower,
    SensorTank[TileTurbineController]("turbine.fuel", "fuelTank", _.fuel)
  )

  val steamTurbineSensors = List(
    DisabledSensor,
    SensorPower,
    SensorTank[TileSteamTurbineController]("turbine.steam", "steamTank", _.steam),
    SensorNumber[TileSteamTurbineController, Double]("turbine.speed", "turbine", _.speed, Vector(
      ParameterNumber("turbine.speed.stop", "turbineStop", _ <= 0, "0"),
      ParameterNumber("turbine.speed.low", "turbineLow", _ > 0, "0"),
      ParameterNumber("turbine.speed.medium", "turbineMed", _ >= MachineSteamTurbine.effectiveRPM / 2, DecFormat.round(MachineSteamTurbine.effectiveRPM / 2)),
      ParameterNumber("turbine.speed.high", "turbineHigh", _ >= MachineSteamTurbine.effectiveRPM, DecFormat.round(MachineSteamTurbine.effectiveRPM))
    ))
  )

  val exchangerSensors = List(
    DisabledSensor,
    SensorResource[TileExchangerController]("exchanger.hot.input", "hotIn", _.heaterIn),
    SensorResource[TileExchangerController]("exchanger.hot.output", "hotOut", _.coolerOut),
    SensorResource[TileExchangerController]("exchanger.cold.input", "coldIn", _.coolerIn),
    SensorResource[TileExchangerController]("exchanger.cold.output", "coldOut", _.heaterOut),
    SensorNumber[TileExchangerController, Double]("exchanger.heat", "heat", _.heat, Vector(
      ParameterNumber("heat.exchanger.cold", "heatCold", _ <= 0, "0"),
      ParameterNumber("heat.exchanger.low", "heatLow", x => x > 0 && x < MachineExchanger.startHeating, DecFormat.round(MachineExchanger.startHeating)),
      ParameterNumber("heat.exchanger.medium", "heatMed", _ >= MachineExchanger.startHeating, DecFormat.round(MachineExchanger.startHeating)),
      ParameterNumber("heat.exchanger.high", "heatHigh", _ >= MachineExchanger.maxHeat, DecFormat.round(MachineExchanger.maxHeat))
    ))
  )

  val syngasSensors = List(
    DisabledSensor,
    SensorTank[TileSyngasController]("syngas.water", "waterTank", _.waterTank),
    SensorTank[TileSyngasController]("syngas.syngas", "fuelTank", _.syngasTank),
    SensorFakeTank[TileSyngasController]("syngas.steam", "steamTank", _.steamBuffer, _.cfg.internalTankCapacity),
    SensorFakeTank[TileSyngasController]("syngas.carbon", "carbonTank", _.carbonBuffer, _.cfg.internalTankCapacity),
    SensorNumber[TileSyngasController, Double]("syngas.heat", "heat", _.heat, Vector(
      ParameterNumber("heat.syngas.cold", "heatCold", _ <= 0, "0"),
      ParameterNumber("heat.syngas.low", "heatLow", x => x > 0 && x < MachineSyngas.workHeat, DecFormat.round(MachineSyngas.workHeat)),
      ParameterNumber("heat.syngas.medium", "heatMed", _ >= MachineSyngas.workHeat, DecFormat.round(MachineSyngas.workHeat)),
      ParameterNumber("heat.syngas.high", "heatHigh", _ >= MachineSyngas.maxHeat, DecFormat.round(MachineSyngas.maxHeat))
    ))
  )
}
