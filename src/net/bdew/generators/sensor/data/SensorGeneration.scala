/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.sensor.data

import net.bdew.generators.config.Config
import net.bdew.generators.controllers.turbine.TileTurbineController
import net.bdew.generators.sensor.{CastSensor, Icons, Sensors}
import net.bdew.lib.sensors.GenericSensorParameter
import net.bdew.lib.{DecFormat, Misc}
import net.minecraft.tileentity.TileEntity

object SensorGeneration extends CastSensor[TileTurbineController] with Icons.Loader {
  override def iconName = "turbine"
  override def uid = "turbine.generation"

  case class GenerationParameter(uid: String, iconName: String, test: Double => Boolean, display: TileTurbineController => String) extends Sensors.SimpleParameter with Icons.Loader

  override val parameters = Vector(
    GenerationParameter("turbine.generation.stop", "turbineStop", _ <= 0, x => "0"),
    GenerationParameter("turbine.generation.low", "turbineLow", _ > 0, x => "0"),
    GenerationParameter("turbine.generation.medium", "turbineMed", _ > 0.5D, x => DecFormat.round(x.maxMJPerTick / 2 * Config.powerShowMultiplier)),
    GenerationParameter("turbine.generation.high", "turbineHigh", _ >= 1, x => DecFormat.round(x.maxMJPerTick * Config.powerShowMultiplier))
  )

  override def getResultTyped(param: GenericSensorParameter, te: TileTurbineController) = param match {
    case x: GenerationParameter =>
      x.test(te.outputAverage.average / te.maxMJPerTick)
    case _ => false
  }

  override def getParamTooltip(obj: TileEntity, param: GenericSensorParameter): List[String] = (obj, param) match {
    case (te: TileTurbineController, x: GenerationParameter) =>
      List(Misc.toLocalF("advgenerators.sensor.param." + param.uid, x.display(te), Config.powerShowUnits))
    case _ => super.getParamTooltip(obj, param)
  }
}
