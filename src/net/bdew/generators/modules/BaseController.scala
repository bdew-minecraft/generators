/*
 * Copyright (c) bdew, 2014 - 2017
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules

import net.bdew.generators.GeneratorsResourceProvider
import net.bdew.lib.multiblock.block.BlockController
import net.bdew.lib.multiblock.tile.TileController
import net.minecraft.block.material.Material

class BaseController[T <: TileController](name: String, TEClass: Class[T]) extends BlockController(name, Material.IRON, TEClass) {
  override def resources = GeneratorsResourceProvider
  setHardness(1)
}
