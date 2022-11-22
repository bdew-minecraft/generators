package net.bdew.generators.integration.mekanism

import net.bdew.generators.integration.mekanism.gasInput.{BlockGasInput, TileGasInput}
import net.bdew.generators.registries.Machines
import net.minecraftforge.data.loading.DatagenModLoader
import net.minecraftforge.fml.ModList
import org.apache.logging.log4j.{LogManager, Logger}

object MekanismIntegration {
  val log: Logger = LogManager.getLogger

  lazy val isAvailable: Boolean = {
    if (DatagenModLoader.isRunningDataGen) {
      log.info("Mekanism integration enabled in datagen")
      true
    } else if (ModList.get.isLoaded("mekanism")) {
      log.info("Mekanism loaded, activating integration")
      true
    } else {
      log.info("Mekanism Not loaded, skipping integration")
      false
    }
  }

  def registerMachines(): Unit = {
    Machines.registerModule("gas_input", () => new BlockGasInput, new TileGasInput(_, _, _))
  }
}
