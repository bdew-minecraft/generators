package net.bdew.generators.controllers

import net.bdew.lib.power.DataSlotPower

trait CIPowerAccess {
  def power: DataSlotPower
}
