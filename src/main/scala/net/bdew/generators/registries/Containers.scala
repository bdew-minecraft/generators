package net.bdew.generators.registries

import net.bdew.generators.controllers.exchanger.{ContainerExchanger, GuiExchanger}
import net.bdew.generators.controllers.steam.{ContainerSteamTurbine, GuiSteamTurbine}
import net.bdew.generators.controllers.syngas.{ContainerSyngas, GuiSyngas}
import net.bdew.generators.controllers.turbine.{ContainerFuelTurbine, GuiFuelTurbine}
import net.bdew.generators.gui.ContainerOutputConfig
import net.bdew.generators.modules.control.{ContainerControl, GuiControl}
import net.bdew.generators.modules.sensor.{ContainerSensor, GuiSensor}
import net.bdew.lib.managers.ContainerManager
import net.minecraft.world.inventory.MenuType
import net.minecraftforge.api.distmarker.{Dist, OnlyIn}
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.registries.RegistryObject

object Containers extends ContainerManager {
  val fuelTurbine: RegistryObject[MenuType[ContainerFuelTurbine]] =
    registerPositional("fuel_turbine", Machines.controllerFuelTurbine.teType) {
      (id, inv, te) => new ContainerFuelTurbine(te, inv, id)
    }

  val steamTurbine: RegistryObject[MenuType[ContainerSteamTurbine]] =
    registerPositional("steam_trubine", Machines.controllerSteamTurbine.teType) {
      (id, inv, te) => new ContainerSteamTurbine(te, inv, id)
    }

  val syngas: RegistryObject[MenuType[ContainerSyngas]] =
    registerPositional("syngas", Machines.controllerSyngas.teType) {
      (id, inv, te) => new ContainerSyngas(te, inv, id)
    }

  val exchanger: RegistryObject[MenuType[ContainerExchanger]] =
    registerPositional("exchanger", Machines.controllerExchanger.teType) {
      (id, inv, te) => new ContainerExchanger(te, inv, id)
    }

  val control: RegistryObject[MenuType[ContainerControl]] =
    registerPositional("control", Machines.moduleControl.teType) {
      (id, inv, te) => new ContainerControl(te, inv, id)
    }

  val sensor: RegistryObject[MenuType[ContainerSensor]] =
    registerPositional("sensor", Machines.moduleSensor.teType) {
      (id, inv, te) => new ContainerSensor(te, inv, id)
    }

  val outputConfig: RegistryObject[MenuType[ContainerOutputConfig]] =
    registerSimple("output_config") { (_, _, _) => new ContainerOutputConfig() }

  @OnlyIn(Dist.CLIENT)
  override def onClientSetup(ev: FMLClientSetupEvent): Unit = {
    registerScreen(fuelTurbine) { (c, i, _) => new GuiFuelTurbine(c, i) }
    registerScreen(steamTurbine) { (c, i, _) => new GuiSteamTurbine(c, i) }
    registerScreen(syngas) { (c, i, _) => new GuiSyngas(c, i) }
    registerScreen(exchanger) { (c, i, _) => new GuiExchanger(c, i) }
    registerScreen(control) { (c, i, _) => new GuiControl(c, i) }
    registerScreen(sensor) { (c, i, _) => new GuiSensor(c, i) }
  }
}
