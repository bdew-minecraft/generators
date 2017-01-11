/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators

import java.io.File

import net.bdew.generators.config.Config
import net.bdew.generators.gui.{CarbonValueTooltipProvider, FuelTooltipProvider}
import net.bdew.generators.model.ExtendedModelLoader
import net.bdew.lib.tooltip.TooltipHandler
import net.minecraftforge.client.model.ModelLoaderRegistry

object GeneratorsClient {
  def preInit(): Unit = {
    Config.load(new File(Generators.configDir, "client.config"))
    sensor.Icons.init()
    control.Icons.init()
    TooltipHandler.register(FuelTooltipProvider)
    TooltipHandler.register(CarbonValueTooltipProvider)
    ModelLoaderRegistry.registerLoader(ExtendedModelLoader)
  }
}
