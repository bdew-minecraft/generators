package net.bdew.generators.network

import net.bdew.lib.network.NetChannel

object NetworkHandler extends NetChannel("generators", "1") {
  regServerContainerHandler(1, classOf[PktDumpBuffers], classOf[ContainerCanDumpBuffers]) { (_, c, _) =>
    c.dumpBuffers()
  }
}

case class PktDumpBuffers() extends NetworkHandler.Message
