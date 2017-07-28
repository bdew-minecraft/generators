/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.compat.opencomputers

import li.cil.oc.api.driver.DriverBlock
import li.cil.oc.api.network.ManagedEnvironment
import net.bdew.lib.computers.ModuleSelector
import net.bdew.lib.multiblock.tile.{TileController, TileModule}
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

import scala.reflect.ClassTag

class BlockDriverSelective[M <: TileModule : ClassTag](selectors: List[ModuleSelector[M, _ <: TileController]]) extends DriverBlock {
  def findSelector(te: M) = selectors.find(_.matches(te))

  override def worksWith(world: World, pos: BlockPos, side: EnumFacing): Boolean = {
    world.getTileEntity(pos) match {
      case x: M => findSelector(x).isDefined
      case _ => false
    }
  }

  override def createEnvironment(world: World, pos: BlockPos, side: EnumFacing): ManagedEnvironment = {
    world.getTileEntity(pos) match {
      case tile: M => (findSelector(tile) map (selector => new ManagedEnvironmentProvider[M](selector.kind, selector.commands, tile))).orNull
      case _ => null
    }
  }
}