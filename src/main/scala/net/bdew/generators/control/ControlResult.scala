package net.bdew.generators.control

object ControlResult extends Enumeration {
  val ENABLED, DISABLED, NEUTRAL = Value

  def fromBool(b: Boolean): ControlResult.Value = if (b) ENABLED else DISABLED
}
