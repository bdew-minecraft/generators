package net.bdew.generators.registries

import net.bdew.generators.GeneratorsResourceProvider
import net.bdew.generators.config.Config
import net.bdew.generators.controllers.exchanger.{BlockExchangerController, ConfigExchanger, OutputSlotsExchanger, TileExchangerController}
import net.bdew.generators.controllers.steam.{BlockSteamTurbineController, ConfigSteamTurbine, TileSteamTurbineController}
import net.bdew.generators.controllers.syngas.{BlockSyngasController, ConfigSyngas, OutputSlotsSyngas, TileSyngasController}
import net.bdew.generators.controllers.turbine.{BlockFuelTurbineController, ConfigFuelTurbine, TileFuelTurbineController}
import net.bdew.generators.modules.control.{BlockControl, TileControl}
import net.bdew.generators.modules.efficiency._
import net.bdew.generators.modules.exchanger.{BlockExchanger, TileExchanger}
import net.bdew.generators.modules.fluidInput.{BlockFluidInput, TileFluidInput}
import net.bdew.generators.modules.fluidOutputSelect.{BlockFluidOutputSelect, TileFluidOutputSelect}
import net.bdew.generators.modules.forgeOutput.{BlockForgeOutput, BlockItemForgeOutput, TileForgeOutput}
import net.bdew.generators.modules.fuelTank.{BlockFuelTank, BlockItemFuelTank, TileFuelTank}
import net.bdew.generators.modules.gasInput.{BlockGasInput, TileGasInput}
import net.bdew.generators.modules.heatingChamber.{BlockHeatingChamber, TileHeatingChamber}
import net.bdew.generators.modules.itemInput.{BlockItemInput, TileItemInput}
import net.bdew.generators.modules.itemOutput.{BlockItemOutput, TileItemOutput}
import net.bdew.generators.modules.mixingChamber.{BlockMixingChamber, TileMixingChamber}
import net.bdew.generators.modules.powerCapacitor.{BlockCapacitor, BlockItemCapacitor, TileCapacitor}
import net.bdew.generators.modules.sensor.{BlockSensor, TileSensor}
import net.bdew.generators.modules.turbine.{BlockItemTurbine, BlockTurbine, TileTurbine}
import net.bdew.lib.multiblock.data.{OutputConfigFluidSlots, OutputConfigManager}
import net.bdew.lib.multiblock.item.{ControllerBlockItem, ModuleBlockItem}
import net.bdew.lib.multiblock.{MultiblockMachineManager, ResourceProvider}
import net.minecraftforge.fml.{DatagenModLoader, ModList}

object Machines extends MultiblockMachineManager(Blocks) {
  override def resources: ResourceProvider = GeneratorsResourceProvider

  val controllerFuelTurbine: ControllerDef[BlockFuelTurbineController, TileFuelTurbineController, ControllerBlockItem, ConfigFuelTurbine] =
    registerController("fuel_turbine_controller",
      () => new BlockFuelTurbineController,
      new TileFuelTurbineController(_),
      Config.FuelTurbine
    )

  val controllerSteamTurbine: Machines.ControllerDef[BlockSteamTurbineController, TileSteamTurbineController, ControllerBlockItem, ConfigSteamTurbine] =
    registerController("steam_turbine_controller",
      () => new BlockSteamTurbineController,
      new TileSteamTurbineController(_),
      Config.SteamTurbine
    )

  val controllerSyngas: Machines.ControllerDef[BlockSyngasController, TileSyngasController, ControllerBlockItem, ConfigSyngas] =
    registerController("syngas_controller",
      () => new BlockSyngasController,
      new TileSyngasController(_),
      Config.SyngasProducer
    )

  val controllerExchanger: Machines.ControllerDef[BlockExchangerController, TileExchangerController, ControllerBlockItem, ConfigExchanger] =
    registerController("exchanger_controller",
      () => new BlockExchangerController,
      new TileExchangerController(_),
      Config.HeatExchanger
    )

  registerModule("fe_output", () => new BlockForgeOutput, new TileForgeOutput(_), new BlockItemForgeOutput(_))
  registerModule("fluid_input", () => new BlockFluidInput, new TileFluidInput(_))

  registerModule("fuel_tank",
    () => new BlockFuelTank(Config.Modules.fuelTank),
    new TileFuelTank(_),
    new BlockItemFuelTank(_)
  )

  registerModule("turbine_tier1",
    () => new BlockTurbine(Config.Modules.turbineTier1),
    new TileTurbine(_),
    new BlockItemTurbine(_)
  )

  registerModule("turbine_tier2",
    () => new BlockTurbine(Config.Modules.turbineTier2),
    new TileTurbine(_),
    new BlockItemTurbine(_)
  )

  registerModule("turbine_tier3",
    () => new BlockTurbine(Config.Modules.turbineTier3),
    new TileTurbine(_),
    new BlockItemTurbine(_)
  )

  registerModule("turbine_tier4",
    () => new BlockTurbine(Config.Modules.turbineTier4),
    new TileTurbine(_),
    new BlockItemTurbine(_)
  )

  registerModule("turbine_tier5",
    () => new BlockTurbine(Config.Modules.turbineTier5),
    new TileTurbine(_),
    new BlockItemTurbine(_)
  )

  registerModule("power_capacitor_tier1",
    () => new BlockCapacitor(Config.Modules.capacitorTier1),
    new TileCapacitor(_),
    new BlockItemCapacitor(_)
  )

  registerModule("power_capacitor_tier2",
    () => new BlockCapacitor(Config.Modules.capacitorTier2),
    new TileCapacitor(_),
    new BlockItemCapacitor(_)
  )

  registerModule("power_capacitor_tier3",
    () => new BlockCapacitor(Config.Modules.capacitorTier3),
    new TileCapacitor(_),
    new BlockItemCapacitor(_)
  )

  registerModule("efficiency_upgrade_tier1",
    () => new BlockEfficiencyUpgradeTier1,
    new TileEfficiencyUpgradeTier1(_),
    new BlockItemEfficiencyUpgradeTier1(_)
  )

  registerModule("efficiency_upgrade_tier2",
    () => new BlockEfficiencyUpgradeTier2,
    new TileEfficiencyUpgradeTier2(_),
    new BlockItemEfficiencyUpgradeTier2(_)
  )

  registerModule("fluid_output_select", () => new BlockFluidOutputSelect, new TileFluidOutputSelect(_))
  registerModule("item_input", () => new BlockItemInput, new TileItemInput(_))
  registerModule("item_output", () => new BlockItemOutput, new TileItemOutput(_))

  registerModule("heating_chamber", () => new BlockHeatingChamber, new TileHeatingChamber(_))
  registerModule("mixing_chamber", () => new BlockMixingChamber, new TileMixingChamber(_))

  registerModule("heat_exchanger", () => new BlockExchanger, new TileExchanger(_))

  val moduleControl: Machines.ModuleDef[BlockControl, TileControl, ModuleBlockItem] =
    registerModule("control", () => new BlockControl, new TileControl(_))

  val moduleSensor: Machines.ModuleDef[BlockSensor, TileSensor, ModuleBlockItem] =
    registerModule("sensor", () => new BlockSensor, new TileSensor(_))

  if (ModList.get.isLoaded("mekanism") || DatagenModLoader.isRunningDataGen) {
    registerModule("gas_input", () => new BlockGasInput, new TileGasInput(_))
  }

  OutputConfigManager.register(OutputSlotsSyngas.outputConfigId, () => new OutputConfigFluidSlots(OutputSlotsSyngas))
  OutputConfigManager.register(OutputSlotsExchanger.outputConfigId, () => new OutputConfigFluidSlots(OutputSlotsExchanger))
}
