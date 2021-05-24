package net.bdew.generators.sensor.data

import net.bdew.generators.sensor.{Icons, Sensors}

case class ParameterFill(uid: String, iconName: String, test: (Double, Double) => Boolean) extends Sensors.SimpleParameter with Icons.Loader

object ParameterFill {
  val empty: ParameterFill = ParameterFill("fill.empty", "fillEmpty", (n, _) => n <= 0)
  val nonEmpty: ParameterFill = ParameterFill("fill.not.empty", "fillNotEmpty", (n, _) => n > 0)
  val gt5: ParameterFill = ParameterFill("fill.gt5", "fill5", _ / _ >= 0.05D)
  val gt25: ParameterFill = ParameterFill("fill.gt25", "fill25", _ / _ >= 0.25D)
  val gt50: ParameterFill = ParameterFill("fill.gt50", "fill50", _ / _ >= 0.50D)
  val gt75: ParameterFill = ParameterFill("fill.gt75", "fill75", _ / _ >= 0.75D)
  val gt95: ParameterFill = ParameterFill("fill.gt95", "fill95", _ / _ >= 0.95D)
  val full: ParameterFill = ParameterFill("fill.full", "fillFull", _ >= _)
  val nonFull: ParameterFill = ParameterFill("fill.not.full", "fillNotFull", _ < _)
}
