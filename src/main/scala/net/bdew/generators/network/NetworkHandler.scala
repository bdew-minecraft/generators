package net.bdew.generators.network

import net.bdew.lib.network.NetChannel
import net.minecraft.network.PacketBuffer

object NetworkHandler extends NetChannel("generators", "2") {
  regServerContainerHandler(1, CodecDumpBuffers, classOf[ContainerCanDumpBuffers]) { (_, c, _) =>
    c.dumpBuffers()
  }
}

case class PktDumpBuffers() extends NetworkHandler.Message

object CodecDumpBuffers extends NetworkHandler.Codec[PktDumpBuffers] {
  override def encodeMsg(m: PktDumpBuffers, p: PacketBuffer): Unit = {}
  override def decodeMsg(p: PacketBuffer): PktDumpBuffers = PktDumpBuffers()
}