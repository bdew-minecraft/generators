/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.compat.computercraft

import dan200.computercraft.api.peripheral.IPeripheralProvider
import net.bdew.lib.computers.ModuleSelector
import net.bdew.lib.multiblock.tile.{TileController, TileModule}
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

import scala.reflect.ClassTag

class PeripheralProviderSelective[M <: TileModule : ClassTag](selectors: List[ModuleSelector[M, _ <: TileController]]) extends IPeripheralProvider {
  override def getPeripheral(world: World, pos: BlockPos, side: EnumFacing) = {
    world.getTileEntity(pos) match {
      case te: M =>
        (selectors.find(_.matches(te)) map (selector => TilePeripheralWrapper(selector.kind, selector.commands, te))).orNull
      case _ => null
    }
  }
}
