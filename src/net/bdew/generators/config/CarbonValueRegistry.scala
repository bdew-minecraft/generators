/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.config

import net.minecraft.item.{Item, ItemStack}
import net.minecraftforge.oredict.OreDictionary

import scala.collection.mutable

object CarbonValueRegistry {
  val values = mutable.Map.empty[Item, mutable.Map[Int, Int]]

  def register(stack: ItemStack, value: Int): Unit = register(stack.getItem, stack.getItemDamage, value)

  def register(item: Item, damage: Int, value: Int) {
    if (values.contains(item)) {
      values(item) += (damage -> value)
    } else {
      values.put(item, mutable.Map(damage -> value))
    }
  }

  def getValueOpt(item: ItemStack) =
    values get item.getItem flatMap { sub =>
      sub.get(item.getItemDamage) orElse sub.get(OreDictionary.WILDCARD_VALUE)
    }

  def getValue(item: ItemStack) = getValueOpt(item) getOrElse 0
}
