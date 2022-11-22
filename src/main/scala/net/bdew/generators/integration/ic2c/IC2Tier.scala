package net.bdew.generators.integration.ic2c

import ic2.api.energy.EnergyNet

sealed case class IC2Tier(number: Int) {
  lazy val maxPower: Int = EnergyNet.INSTANCE.getPowerFromTier(number)
  lazy val name: String = EnergyNet.INSTANCE.getDisplayTier(number)
}

object IC2Tier {
  val LV: IC2Tier = IC2Tier(1)
  val MV: IC2Tier = IC2Tier(2)
  val HV: IC2Tier = IC2Tier(3)
  val EV: IC2Tier = IC2Tier(4)
  val IV: IC2Tier = IC2Tier(5)
}
