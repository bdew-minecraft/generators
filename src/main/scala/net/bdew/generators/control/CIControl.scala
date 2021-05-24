package net.bdew.generators.control

import net.bdew.lib.multiblock.tile.TileController

trait CIControl extends TileController {
  var controlState: Map[ControlAction, ControlResult.Value] = Map.empty[ControlAction, ControlResult.Value].withDefaultValue(ControlResult.NEUTRAL)

  def availableControlActions: List[ControlAction]

  def onControlStateChanged(): Unit = {
    val controlModules = getModuleTiles[MIControl]
    val result = for (action <- availableControlActions) yield {
      val results = controlModules.map(_.getControlState(action))
      action -> (
        if (results.contains(ControlResult.ENABLED))
          ControlResult.ENABLED
        else if (results.contains(ControlResult.DISABLED))
          ControlResult.DISABLED
        else
          ControlResult.NEUTRAL
        )
    }
    controlState = result.toMap.withDefaultValue(ControlResult.NEUTRAL)
  }

  def getControlStateWithDefault(action: ControlAction, default: => Boolean): Boolean =
    controlState(action) match {
      case ControlResult.DISABLED => false
      case ControlResult.ENABLED => true
      case ControlResult.NEUTRAL => default
    }

  override def onModulesChanged(): Unit = {
    onControlStateChanged()
  }
}