package net.bdew.generators.network

import net.bdew.lib.container.NoInvContainer

trait ContainerCanDumpBuffers extends NoInvContainer {
  def dumpBuffers(): Unit
}
