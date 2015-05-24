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
  final val IC2_MOD_ID = "IC2"
  final val TE_MOD_ID = "CoFHAPI"

  lazy val EUEnabled = Tuning.getSection("Power").getSection("EU").getBoolean("Enabled")
  lazy val RFEnabled = Tuning.getSection("Power").getSection("RF").getBoolean("Enabled")

  lazy val haveIC2 = Misc.haveModVersion(IC2_MOD_ID)
  lazy val haveTE = Misc.haveModVersion(TE_MOD_ID)
  lazy val haveBCFuel = Misc.haveModVersion("BuildCraftAPI|fuels@[2.0,)")
  lazy val haveMekanismGasApi = Misc.haveModVersion("MekanismAPI|gas@[8.0.0,)")

  def logModVersions() {
    if (!haveIC2 && !haveTE) {
      Generators.logWarn("No useable energy system detected")
      Generators.logWarn("This mod requires at least one of the following mods to function properly:")
      Generators.logWarn("* CoFHCore (or any mod that includes the API)")
      Generators.logWarn("* IC2 Experimental")
    }
    Generators.logInfo("IC2 Version: %s", Misc.getModVersionString(IC2_MOD_ID))
    Generators.logInfo("RF Version: %s", Misc.getModVersionString(TE_MOD_ID))
  }
}
