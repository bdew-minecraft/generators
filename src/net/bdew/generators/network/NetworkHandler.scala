/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.network

import net.bdew.generators.Generators
import net.bdew.lib.network.NetChannel

object NetworkHandler extends NetChannel(Generators.channel) {
  regServerHandler {
    case (PktDumpBuffers(), player) =>
      if (player.openContainer.isInstanceOf[ContainerCanDumpBuffers])
        player.openContainer.asInstanceOf[ContainerCanDumpBuffers].dumpBuffers()
  }
}

