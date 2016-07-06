/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.config

import net.bdew.generators.Generators
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntityFurnace
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCMessage

object IMC {
  def processMessage(msg: IMCMessage): Unit = {
    try {
      msg.key match {
        case "AddCarbonValue" if msg.isNBTMessage =>
          processAddCarbonValue(msg.getNBTValue, msg.getSender)
        case _ =>
          Generators.logWarn("Unable to process messgae %s from %s (%s)", msg.key, msg.getSender, msg.getMessageType.getName)
      }
    } catch {
      case e: Throwable =>
        Generators.logErrorException("Error processing IMC message from %s", e, msg.getSender)
    }
  }

  def processAddCarbonValue(tag: NBTTagCompound, sender: String): Unit = {
    val item = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("item"))
    val value = if (tag.getBoolean("useBurnTime")) {
      TileEntityFurnace.getItemBurnTime(item)
    } else {
      tag.getInteger("value")
    }
    if (value > 0)
      CarbonValueRegistry.register(item, value)
    else
      Generators.logWarn("Mod %s attempting to register carbon source %s with no valid carbon value", sender, item)
  }
}
