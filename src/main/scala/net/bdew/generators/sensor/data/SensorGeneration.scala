package net.bdew.generators.sensor.data

import net.bdew.generators.controllers.turbine.TileFuelTurbineController
import net.bdew.generators.sensor.{CastSensor, Icons, Sensors}
import net.bdew.lib.Text
import net.bdew.lib.sensors.GenericSensorParameter
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.text.ITextComponent

object SensorGeneration extends CastSensor[TileFuelTurbineController] with Icons.Loader {
  override def iconName = "turbine"
  override def uid = "turbine.generation"

  case class GenerationParameter(uid: String, iconName: String, test: Double => Boolean, display: TileFuelTurbineController => ITextComponent) extends Sensors.SimpleParameter with Icons.Loader

  override val parameters = Vector(
    GenerationParameter("turbine.generation.stop", "turbineStop", _ <= 0, x => Text.energyPerTick(0)),
    GenerationParameter("turbine.generation.low", "turbineLow", _ > 0, x => Text.energyPerTick(0)),
    GenerationParameter("turbine.generation.medium", "turbineMed", _ > 0.5D, x => Text.energyPerTick(x.maxFEPerTick / 2)),
    GenerationParameter("turbine.generation.high", "turbineHigh", _ >= 1, x => Text.energyPerTick(x.maxFEPerTick))
  )

  override def getResultTyped(param: GenericSensorParameter, te: TileFuelTurbineController): Boolean = param match {
    case x: GenerationParameter =>
      x.test(te.outputAverage.average / te.maxFEPerTick)
    case _ => false
  }

  override def getParamTooltip(obj: TileEntity, param: GenericSensorParameter): List[ITextComponent] = (obj, param) match {
    case (te: TileFuelTurbineController, x: GenerationParameter) =>
      List(Text.translate("advgenerators.sensor.param." + param.uid, x.display(te)))
    case _ => super.getParamTooltip(obj, param)
  }
}
