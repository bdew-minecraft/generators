/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.config

import net.bdew.generators.Generators
import net.bdew.generators.items.{TurbineItem, TurbineUpgradeKit}
import net.bdew.generators.modules.turbine.BlockTurbine
import net.bdew.lib.Misc
import net.bdew.lib.recipes.gencfg.ConfigSection
import net.minecraftforge.oredict.OreDictionary

case class TurbineMaterial(name: String, tier: Int, maxMJPerTick: Double, inertiaMultiplier: Double) {
  var bladeItem: Option[TurbineItem] = None
  var rotorItem: Option[TurbineItem] = None
  var kitItem: Option[TurbineItem] = None
  var turbineBlock: Option[BlockTurbine] = None
}

object TurbineMaterials {
  var registry = Map.empty[String, TurbineMaterial]
  def materials = registry.values.toSet

  def init(): Unit = {
    for ((name, cfg) <- Tuning.getSection("TurbineMaterials").filterType(classOf[ConfigSection])) {
      if (cfg.hasValue("ReqOreDict") && OreDictionary.getOres(cfg.getString("ReqOreDict")).isEmpty) {
        Generators.logInfo("Turbine material %s not present - skipping", name)
      } else if (cfg.hasValue("ReqMod") && !Misc.haveModVersion(cfg.getString("ReqMod"))) {
        Generators.logInfo("Mod %s for turbine material %s not present - skipping", cfg.getString("ReqMod"), name)
      } else {
        Generators.logInfo("Registering turbine material: %s", name)
        val material = TurbineMaterial(name,
          tier = cfg.getInt("Tier"),
          maxMJPerTick = cfg.getDouble("MaxMJPerTick"),
          inertiaMultiplier = cfg.getDouble("InertiaMultiplier")
        )
        registry += name -> material

        if ((!cfg.hasValue("RegisterBlade")) || cfg.getBoolean("RegisterBlade"))
          material.bladeItem = Some(Items.regItem(new TurbineItem(material, "Blade")))
        if ((!cfg.hasValue("RegisterRotor")) || cfg.getBoolean("RegisterRotor"))
          material.rotorItem = Some(Items.regItem(new TurbineItem(material, "Rotor")))
        if ((!cfg.hasValue("RegisterKit")) || cfg.getBoolean("RegisterKit"))
          material.kitItem = Some(Items.regItem(new TurbineUpgradeKit(material)))

        // TE's are shared between all turbines so they don't need to be registered here
        material.turbineBlock = Some(Blocks.regSpecial(new BlockTurbine(material), skipTileEntityReg = true))
      }
    }
  }
}
