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
import net.bdew.generators.items.CapacitorUpgradeKit
import net.bdew.generators.modules.powerCapacitor.BlockPowerCapacitor
import net.bdew.lib.recipes.gencfg.ConfigSection
import net.minecraftforge.oredict.OreDictionary

case class CapacitorMaterial(name: String, tier: Int, mjCapacity: Double) {
  var kitItem: Option[CapacitorUpgradeKit] = None
  var capacitorBlock: Option[BlockPowerCapacitor] = None
}

object CapacitorMaterials {
  var registry = Map.empty[String, CapacitorMaterial]
  def materials = registry.values.toSet

  def init(): Unit = {
    for ((name, cfg) <- Tuning.getSection("CapacitorMaterials").filterType(classOf[ConfigSection])) {
      if (cfg.hasValue("ReqOreDict") && OreDictionary.getOres(cfg.getString("ReqOreDict")).isEmpty) {
        Generators.logInfo("Capacitor material %s not present - skipping", name)
      } else {
        Generators.logInfo("Registering capacitor material: %s", name)
        val material = CapacitorMaterial(name,
          tier = cfg.getInt("Tier"),
          mjCapacity = cfg.getDouble("MJCapacity")
        )
        registry += name -> material

        if ((!cfg.hasValue("RegisterKit")) || cfg.getBoolean("RegisterKit"))
          material.kitItem = Some(Items.regItem(new CapacitorUpgradeKit(material)))

        // TE's are shared between all turbines so they don't need to be registered here
        material.capacitorBlock = Some(Blocks.regBlock(new BlockPowerCapacitor(material), skipTileEntityReg = true))
      }
    }
  }
}
