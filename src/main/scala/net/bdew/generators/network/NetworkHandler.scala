package net.bdew.generators.network

import net.bdew.generators.Generators
import net.bdew.lib.network.NetChannel
import net.minecraft.network.FriendlyByteBuf

object NetworkHandler extends NetChannel(Generators.ModId, "generators", "2") {
  regServerContainerHandler(1, CodecDumpBuffers, classOf[ContainerCanDumpBuffers]) { (_, c, _) =>
    c.dumpBuffers()
  }
}

case class PktDumpBuffers() extends NetworkHandler.Message

object CodecDumpBuffers extends NetworkHandler.Codec[PktDumpBuffers] {
  override def encodeMsg(m: PktDumpBuffers, p: FriendlyByteBuf): Unit = {}
  override def decodeMsg(p: FriendlyByteBuf): PktDumpBuffers = PktDumpBuffers()
}