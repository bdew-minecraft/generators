package net.bdew.generators.controllers.turbine

import net.bdew.generators.network.ContainerCanDumpBuffers
import net.bdew.generators.registries.Containers
import net.bdew.lib.container.NoInvContainer
import net.bdew.lib.data.base.{ContainerDataSlots, TileDataSlots}
import net.bdew.lib.multiblock.interact.ContainerOutputFaces
import net.minecraft.entity.player.PlayerInventory
import net.minecraftforge.fluids.FluidStack

class ContainerFuelTurbine(val te: TileFuelTurbineController, playerInventory: PlayerInventory, id: Int)
  extends NoInvContainer(Containers.fuelTurbine.get(), id) with ContainerDataSlots with ContainerOutputFaces with ContainerCanDumpBuffers {
  lazy val dataSource: TileDataSlots = te

  bindPlayerInventory(playerInventory, 8, 94, 152)

  override def dumpBuffers(): Unit = {
    te.fuel.setFluid(FluidStack.EMPTY)
  }
}
