package net.bdew.generators.controllers.steam

import net.bdew.generators.network.ContainerCanDumpBuffers
import net.bdew.generators.registries.Containers
import net.bdew.lib.container.NoInvContainer
import net.bdew.lib.data.base.ContainerDataSlots
import net.bdew.lib.multiblock.interact.ContainerOutputFaces
import net.minecraft.world.entity.player.Inventory
import net.minecraftforge.fluids.FluidStack

class ContainerSteamTurbine(val te: TileSteamTurbineController, playerInventory: Inventory, id: Int)
  extends NoInvContainer(Containers.steamTurbine.get(), id) with ContainerDataSlots with ContainerOutputFaces with ContainerCanDumpBuffers {
  lazy val dataSource: TileSteamTurbineController = te

  bindPlayerInventory(playerInventory, 8, 94, 152)

  override def dumpBuffers(): Unit = {
    te.steam.setFluid(FluidStack.EMPTY)
  }
}
