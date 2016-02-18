/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.compat

import net.bdew.generators.Generators
import net.bdew.generators.config.Tuning
import net.bdew.lib.Misc

object PowerProxy {
  final val RF_API_ID = "CoFHAPI|energy"

  lazy val RFEnabled = Tuning.getSection("Power").getSection("RF").getBoolean("Enabled")

  lazy val haveRF = Misc.haveModVersion(RF_API_ID)

  def logModVersions() {
    if (!haveRF) {
      Generators.logWarn("No useable energy system detected")
      Generators.logWarn("This mod requires at least one of the following mods/APIs to function properly:")
      Generators.logWarn("* Official RF API")
    }
    Generators.logInfo("RF Version: %s", Misc.getModVersionString(RF_API_ID))
  }
}
