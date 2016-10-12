/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.compat.opencomputers

import li.cil.oc.api.Driver
import net.bdew.generators.modules.dataport._

object OCBlocks {
  def init(): Unit = {
    Driver.add(new BlockDriverSelective[TileDataPort](BlockDataPort.selectors))
  }
}
