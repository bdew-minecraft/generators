package net.bdew.generators.control

case class ControlAction(uid: String, iconName: String) extends Icons.Loader {
  ControlActions.registry += uid -> this
}

object ControlActions {
  var registry = Map.empty[String, ControlAction]

  val disabled: ControlAction = ControlAction("disabled", "disabled")
  val useSteam: ControlAction = ControlAction("useSteam", "steam")
  val useFuel: ControlAction = ControlAction("useFuel", "fuel")
  val generateEnergy: ControlAction = ControlAction("generateEnergy", "energy")
  val exchangeHeat: ControlAction = ControlAction("exchangeHeat", "exchange")
  val mix: ControlAction = ControlAction("mix", "mixing")
  val heatWater: ControlAction = ControlAction("heatWater", "heating")
}