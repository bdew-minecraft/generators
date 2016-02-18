/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules

import net.bdew.generators.GeneratorsResourceProvider
import net.bdew.generators.config.Machines
import net.bdew.lib.multiblock.block.BlockModule
import net.bdew.lib.multiblock.tile.TileModule
import net.minecraft.block.material.Material

class BaseModule[T <: TileModule](name: String, kind: String, TEClass: Class[T]) extends BlockModule(name, kind, Material.iron, TEClass, Machines) {
  override def resources = GeneratorsResourceProvider
  setHardness(1)
}
