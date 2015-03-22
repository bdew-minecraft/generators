/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.sensor

import net.bdew.generators.sensor.Sensors
import net.bdew.lib.Misc
import net.bdew.lib.multiblock.tile.TileController
import net.bdew.lib.sensors._
import net.bdew.lib.sensors.multiblock.{CIRedstoneSensors, TileRedstoneSensorModule}
import net.minecraft.block.Block
import net.minecraft.world.World

class TileSensor extends TileRedstoneSensorModule {
  override val kind = "Sensor"
  override val config = DataSlotSensor(Sensors, "sensor", this, Sensors.DisabledSensor)
  override def getCore = getCoreAs[CIRedstoneSensors]

  override def connect(target: TileController) = {
    super.connect(target)
    for {
      core <- Misc.asInstanceOpt(target, classOf[CIRedstoneSensors])
      sensor <- core.sensorTypes.headOption
    } {
      config := SensorPair(sensor, sensor.defaultParameter)
    }
  }
  override def coreRemoved() = {
    config := Sensors.DisabledSensorPair
    super.coreRemoved()
  }

  def isSignalOn = BlockSensor.isSignalOn(worldObj, xCoord, yCoord, zCoord)

  serverTick.listen(() => {
    val act = getCore exists config.getResult
    if (BlockSensor.isSignalOn(worldObj, xCoord, yCoord, zCoord) != act) {
      BlockSensor.setSignal(worldObj, xCoord, yCoord, zCoord, act)
    }
  })

  override def shouldRefresh(oldBlock: Block, newBlock: Block, oldMeta: Int, newMeta: Int, world: World, x: Int, y: Int, z: Int) =
    newBlock != oldBlock
}
