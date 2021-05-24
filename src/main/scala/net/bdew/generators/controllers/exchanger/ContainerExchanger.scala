package net.bdew.generators.controllers.exchanger

import net.bdew.generators.network.ContainerCanDumpBuffers
import net.bdew.generators.registries.Containers
import net.bdew.lib.container.NoInvContainer
import net.bdew.lib.data.base.ContainerDataSlots
import net.bdew.lib.multiblock.interact.ContainerOutputFaces
import net.minecraft.entity.player.PlayerInventory

class ContainerExchanger(val te: TileExchangerController, playerInventory: PlayerInventory, id: Int)
  extends NoInvContainer(Containers.exchanger.get(), id) with ContainerDataSlots with ContainerOutputFaces with ContainerCanDumpBuffers {
  lazy val dataSource: TileExchangerController = te

  bindPlayerInventory(playerInventory, 8, 94, 152)

  override def dumpBuffers(): Unit = {
    te.heaterIn.drain(Double.MaxValue, false, true)
    te.coolerIn.drain(Double.MaxValue, false, true)
    te.heaterOut.drain(Double.MaxValue, false, true)
    te.coolerOut.drain(Double.MaxValue, false, true)
  }
}
