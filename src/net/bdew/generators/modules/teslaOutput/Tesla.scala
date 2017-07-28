/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.teslaOutput

import net.darkhax.tesla.api.{ITeslaConsumer, ITeslaHolder, ITeslaProducer}
import net.minecraftforge.common.capabilities.{Capability, CapabilityInject}

import scala.annotation.meta.setter

object Tesla {
  @(CapabilityInject@setter)(classOf[ITeslaConsumer])
  var CONSUMER: Capability[ITeslaConsumer] = null

  @(CapabilityInject@setter)(classOf[ITeslaHolder])
  var HOLDER: Capability[ITeslaHolder] = null

  @(CapabilityInject@setter)(classOf[ITeslaProducer])
  var PRODUCER: Capability[ITeslaProducer] = null
}
