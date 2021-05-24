package net.bdew.generators.sensor

import net.bdew.generators.config.Config
import net.bdew.generators.controllers.exchanger.TileExchangerController
import net.bdew.generators.controllers.steam.TileSteamTurbineController
import net.bdew.generators.controllers.syngas.TileSyngasController
import net.bdew.generators.controllers.turbine.TileFuelTurbineController
import net.bdew.generators.sensor.data._
import net.bdew.lib.DecFormat
import net.bdew.lib.gui.Texture
import net.bdew.lib.sensors.RedstoneSensors
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.api.distmarker.{Dist, OnlyIn}

object Sensors extends RedstoneSensors[TileEntity] {
  @OnlyIn(Dist.CLIENT)
  override def disabledTexture: Texture = Icons.disabled
  override def localizationPrefix = "advgenerators.sensor"

  val fuelTurbineSensors = List(
    DisabledSensor,
    SensorPower,
    SensorTank[TileFuelTurbineController]("turbine.fuel", "fuelTank", _.fuel),
    SensorGeneration
  )

  val steamTurbineSensors = List(
    DisabledSensor,
    SensorPower,
    SensorTank[TileSteamTurbineController]("turbine.steam", "steamTank", _.steam),
    SensorNumber[TileSteamTurbineController, Double]("turbine.speed", "turbine", _.speed, Vector(
      ParameterNumber("turbine.speed.stop", "turbineStop", _ <= 0, "0"),
      ParameterNumber("turbine.speed.low", "turbineLow", _ > 0, "0"),
      ParameterNumber("turbine.speed.medium", "turbineMed", _ >= Config.SteamTurbine.maxRPM() / 2, DecFormat.round(Config.SteamTurbine.maxRPM() / 2)),
      ParameterNumber("turbine.speed.high", "turbineHigh", _ >= Config.SteamTurbine.maxRPM(), DecFormat.round(Config.SteamTurbine.maxRPM()))
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
      ParameterNumber("heat.exchanger.low", "heatLow", x => x > 0 && x < Config.HeatExchanger.startHeating(), DecFormat.round(Config.HeatExchanger.startHeating())),
      ParameterNumber("heat.exchanger.medium", "heatMed", _ >= Config.HeatExchanger.startHeating(), DecFormat.round(Config.HeatExchanger.startHeating())),
      ParameterNumber("heat.exchanger.high", "heatHigh", _ >= Config.HeatExchanger.maxHeat(), DecFormat.round(Config.HeatExchanger.maxHeat()))
    ))
  )

  val syngasSensors = List(
    DisabledSensor,
    SensorTank[TileSyngasController]("syngas.water", "waterTank", _.waterTank),
    SensorTank[TileSyngasController]("syngas.syngas", "fuelTank", _.syngasTank),
    SensorFakeTank[TileSyngasController]("syngas.steam", "steamTank", _.steamBuffer, _.cfg.internalTankCapacity()),
    SensorFakeTank[TileSyngasController]("syngas.carbon", "carbonTank", _.carbonBuffer, _.cfg.internalTankCapacity()),
    SensorNumber[TileSyngasController, Double]("syngas.heat", "heat", _.heat, Vector(
      ParameterNumber("heat.syngas.cold", "heatCold", _ <= 0, "0"),
      ParameterNumber("heat.syngas.low", "heatLow", x => x > 0 && x < Config.SyngasProducer.workHeat(), DecFormat.round(Config.SyngasProducer.workHeat())),
      ParameterNumber("heat.syngas.medium", "heatMed", _ >= Config.SyngasProducer.workHeat(), DecFormat.round(Config.SyngasProducer.workHeat())),
      ParameterNumber("heat.syngas.high", "heatHigh", _ >= Config.SyngasProducer.maxHeat(), DecFormat.round(Config.SyngasProducer.maxHeat()))
    ))
  )
}
