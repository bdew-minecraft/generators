package net.bdew.generators.integration.ic2c

import net.bdew.generators.integration.ic2c.euOutput.{BlockEuOutput, BlockItemEuOutput, TileEuOutput}
import net.bdew.generators.registries.{Blocks, Machines}
import net.minecraftforge.data.loading.DatagenModLoader
import net.minecraftforge.fml.ModList
import org.apache.logging.log4j.{LogManager, Logger}

object IC2CIntegration {
  val log: Logger = LogManager.getLogger

  lazy val isAvailable: Boolean = {
    if (DatagenModLoader.isRunningDataGen) {
      log.info("IC2 integration enabled in datagen")
      true
    } else if (ModList.get().isLoaded("ic2")) {
      try {
        Class.forName("ic2.api.energy.EnergyNet")
        log.info("IC2 loaded, activating integration")
        true
      } catch {
        case err: Throwable =>
          log.warn("IC2 loaded, but failed to access API", err)
          false
      }
    } else {
      log.info("IC2 Not loaded, skipping integration")
      false
    }
  }

  def registerMachines(): Unit = {
    if (DatagenModLoader.isRunningDataGen) {
      // Avoid registering TEs in datagen as we don't have the mod loaded in CI and it will blow up
      Blocks.define("eu_output_lv", () => new BlockEuOutput(IC2Tier.LV)).withItem(new BlockItemEuOutput(_)).register
      Blocks.define("eu_output_mv", () => new BlockEuOutput(IC2Tier.MV)).withItem(new BlockItemEuOutput(_)).register
      Blocks.define("eu_output_hv", () => new BlockEuOutput(IC2Tier.HV)).withItem(new BlockItemEuOutput(_)).register
      Blocks.define("eu_output_ev", () => new BlockEuOutput(IC2Tier.EV)).withItem(new BlockItemEuOutput(_)).register
      Blocks.define("eu_output_iv", () => new BlockEuOutput(IC2Tier.IV)).withItem(new BlockItemEuOutput(_)).register
    } else {
      Machines.registerModule("eu_output_lv", () => new BlockEuOutput(IC2Tier.LV), new TileEuOutput(_, _, _, IC2Tier.LV), new BlockItemEuOutput(_))
      Machines.registerModule("eu_output_mv", () => new BlockEuOutput(IC2Tier.MV), new TileEuOutput(_, _, _, IC2Tier.MV), new BlockItemEuOutput(_))
      Machines.registerModule("eu_output_hv", () => new BlockEuOutput(IC2Tier.HV), new TileEuOutput(_, _, _, IC2Tier.HV), new BlockItemEuOutput(_))
      Machines.registerModule("eu_output_ev", () => new BlockEuOutput(IC2Tier.EV), new TileEuOutput(_, _, _, IC2Tier.EV), new BlockItemEuOutput(_))
      Machines.registerModule("eu_output_iv", () => new BlockEuOutput(IC2Tier.IV), new TileEuOutput(_, _, _, IC2Tier.IV), new BlockItemEuOutput(_))
    }
  }
}
