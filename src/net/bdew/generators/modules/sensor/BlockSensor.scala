/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.sensor

import net.bdew.generators.Generators
import net.bdew.generators.config.Config
import net.bdew.generators.modules.BaseModule
import net.bdew.generators.sensor.Sensors
import net.bdew.lib.sensors.multiblock.{BlockRedstoneSensorModule, TileRedstoneSensorModule}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

class TileSensor extends TileRedstoneSensorModule(Sensors, BlockSensor)

object BlockSensor extends BaseModule("Sensor", "Sensor", classOf[TileSensor]) with BlockRedstoneSensorModule[TileSensor] {
  override def guiId = 5
  override type TEClass = TileSensor

  Config.guiHandler.register(this)

  override def doOpenGui(world: World, pos: BlockPos, player: EntityPlayer) =
    player.openGui(Generators, guiId, world, pos.getX, pos.getY, pos.getZ)

  @SideOnly(Side.CLIENT)
  override def getGui(te: TEClass, player: EntityPlayer) = new GuiSensor(te, player)
  override def getContainer(te: TEClass, player: EntityPlayer) = new ContainerSensor(te, player)
}
