/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.sensor

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.bdew.generators.Generators
import net.bdew.generators.config.Config
import net.bdew.generators.modules.BaseModule
import net.bdew.generators.sensor.Sensors
import net.bdew.lib.Misc
import net.bdew.lib.sensors.multiblock.{BlockRedstoneSensorModule, TileRedstoneSensorModule}
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

class TileSensor extends TileRedstoneSensorModule(Sensors, BlockSensor)

object BlockSensor extends BaseModule("Sensor", "Sensor", classOf[TileSensor]) with BlockRedstoneSensorModule[TileSensor] {
  override def guiId = 5
  override type TEClass = TileSensor

  Config.guiHandler.register(this)

  override def doOpenGui(world: World, x: Int, y: Int, z: Int, player: EntityPlayer) =
    player.openGui(Generators, guiId, world, x, y, z)

  @SideOnly(Side.CLIENT)
  override def getGui(te: TEClass, player: EntityPlayer) = new GuiSensor(te, player)
  override def getContainer(te: TEClass, player: EntityPlayer) = new ContainerSensor(te, player)

  @SideOnly(Side.CLIENT) override
  def registerBlockIcons(reg: IIconRegister): Unit = {
    sideIcon = reg.registerIcon(Misc.iconName(Generators.modId, name, "/side_off"))
    frontIcon = reg.registerIcon(Misc.iconName(Generators.modId, name, "/front_off"))
    bottomIcon = reg.registerIcon(Misc.iconName(Generators.modId, name, "/back"))
    frontIconOn = reg.registerIcon(Misc.iconName(Generators.modId, name, "/front_on"))
    sideIconOn = reg.registerIcon(Misc.iconName(Generators.modId, name, "/side_on"))
  }
}
