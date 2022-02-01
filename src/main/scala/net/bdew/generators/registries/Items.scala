package net.bdew.generators.registries

import net.bdew.generators.items.UpgradeKit
import net.bdew.lib.managers.ItemManager
import net.minecraft.world.item.{CreativeModeTab, Item, ItemStack}
import net.minecraftforge.registries.RegistryObject

object CreativeTab extends CreativeModeTab("bdew.generators") {
  override def makeIcon(): ItemStack = new ItemStack(Machines.controllerFuelTurbine.item.get())
}

object Items extends ItemManager(CreativeTab) {
  def craftingItemProps: Item.Properties = props
  def upgradeKitProps: Item.Properties = props

  simple("iron_frame", craftingItemProps)
  simple("power_io", craftingItemProps)
  simple("iron_tubing", craftingItemProps)
  simple("iron_wiring", craftingItemProps)
  simple("controller", craftingItemProps)
  simple("pressure_valve", craftingItemProps)
  simple("advanced_pressure_valve", craftingItemProps)

  val upgradeKit: RegistryObject[Item] = simple("upgrade_kit", craftingItemProps)

  simple("turbine_blade", craftingItemProps)

  simple("turbine_rotor_tier1", craftingItemProps)
  simple("turbine_rotor_tier2", craftingItemProps)
  simple("turbine_rotor_tier3", craftingItemProps)
  simple("turbine_rotor_tier4", craftingItemProps)
  simple("turbine_rotor_tier5", craftingItemProps)

  register("turbine_kit_tier2", () => new UpgradeKit)
  register("turbine_kit_tier3", () => new UpgradeKit)
  register("turbine_kit_tier4", () => new UpgradeKit)
  register("turbine_kit_tier5", () => new UpgradeKit)

  register("capacitor_kit_tier2", () => new UpgradeKit)
  register("capacitor_kit_tier3", () => new UpgradeKit)
}
