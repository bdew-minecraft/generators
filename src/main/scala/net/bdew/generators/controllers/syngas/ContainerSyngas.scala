package net.bdew.generators.controllers.syngas

import net.bdew.generators.network.ContainerCanDumpBuffers
import net.bdew.generators.registries.Containers
import net.bdew.lib.container.{BaseContainer, SlotValidating}
import net.bdew.lib.data.base.ContainerDataSlots
import net.bdew.lib.multiblock.interact.ContainerOutputFaces
import net.minecraft.entity.player.PlayerInventory
import net.minecraftforge.fluids.FluidStack

class ContainerSyngas(val te: TileSyngasController, playerInventory: PlayerInventory, id: Int)
  extends BaseContainer(te.inventory, Containers.syngas.get(), id) with ContainerDataSlots with ContainerOutputFaces with ContainerCanDumpBuffers {
  lazy val dataSource: TileSyngasController = te

  addSlot(new SlotValidating(te.inventory, 0, 77, 61))
  addSlot(new SlotValidating(te.inventory, 1, 95, 61))
  addSlot(new SlotValidating(te.inventory, 2, 113, 61))
  addSlot(new SlotValidating(te.inventory, 3, 131, 61))

  bindPlayerInventory(playerInventory, 8, 94, 152)

  override def dumpBuffers(): Unit = {
    te.waterTank.setFluid(FluidStack.EMPTY)
    te.syngasTank.setFluid(FluidStack.EMPTY)
    te.steamBuffer := 0
    te.carbonBuffer := 0
    te.heat := 0
  }
}
