/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.waila

import net.bdew.generators.config.Blocks
import net.bdew.generators.controllers.syngas.TileSyngasController
import net.bdew.lib.{DecFormat, Misc}
import net.minecraftforge.fluids.FluidRegistry

object SyngasProducerDataProvider extends BaseControllerDataProvider(classOf[TileSyngasController]) {
  override def getBodyStringsFromTE(te: TileSyngasController) = {
    List(
      "%s/%s mB %s".format(DecFormat.round(te.waterTank.getFluidAmount), DecFormat.round(te.waterTank.getCapacity), FluidRegistry.WATER.getLocalizedName(null)),
      "%s/%s mB %s".format(DecFormat.round(te.syngasTank.getFluidAmount), DecFormat.round(te.syngasTank.getCapacity), Blocks.syngasFluid.getLocalizedName(null)),
      Misc.toLocalF("advgenerators.label.syngas.heat", DecFormat.round(te.heat), DecFormat.round(te.cfg.maxHeat)),
      Misc.toLocalF("advgenerators.label.syngas.steam", DecFormat.short(te.steamBuffer / te.cfg.internalTankCapacity * 100) + "%"),
      Misc.toLocalF("advgenerators.label.syngas.carbon", DecFormat.short(te.carbonBuffer / te.cfg.internalTankCapacity * 100) + "%")
    )
  }
}
