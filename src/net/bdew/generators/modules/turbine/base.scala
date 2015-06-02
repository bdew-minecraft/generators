/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.turbine

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.bdew.generators.config.Tuning
import net.bdew.generators.modules.BaseModule
import net.bdew.lib.multiblock.tile.TileModule
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.util.IIcon
import net.minecraftforge.common.util.ForgeDirection

class BlockTurbineBase[T <: TileTurbineBase](name: String, cls: Class[T]) extends BaseModule("Turbine" + name, "Turbine", cls) {
  var topIcon: IIcon = null

  lazy val cfg = Tuning.getSection("Modules").getSection("Turbine").getSection(name)
  lazy val maxMJPerTick = cfg.getDouble("MaxMJPerTick")
  lazy val inertiaMultiplier = cfg.getDouble("InertiaMultiplier")

  @SideOnly(Side.CLIENT) override
  def registerBlockIcons(ir: IIconRegister): Unit = {
    topIcon = ir.registerIcon("advgenerators:turbine/" + name.toLowerCase + "/top")
    blockIcon = ir.registerIcon("advgenerators:turbine/" + name.toLowerCase + "/main")
  }

  override def getIcon(side: Int, meta: Int) =
    if (side == ForgeDirection.UP.ordinal() || side == ForgeDirection.DOWN.ordinal()) topIcon else blockIcon
}

class TileTurbineBase extends TileModule {
  val kind: String = "Turbine"
}