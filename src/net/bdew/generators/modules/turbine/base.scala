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
import net.bdew.generators.config.{Config, Tuning}
import net.bdew.generators.modules.BaseModule
import net.bdew.lib
import net.bdew.lib.Misc
import net.bdew.lib.block.BlockTooltip
import net.bdew.lib.multiblock.tile.TileModule
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.{EnumChatFormatting, IIcon}
import net.minecraftforge.common.util.ForgeDirection

abstract class BlockTurbineBase[T <: TileTurbineBase](name: String, cls: Class[T]) extends BaseModule("Turbine" + name, "Turbine", cls) with BlockTooltip {
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

  override def getTooltip(stack: ItemStack, player: EntityPlayer, advanced: Boolean): List[String] = {
    List(
      Misc.toLocalF("advgenerators.tooltip.turbine.rf", "%s%s %s/t".format(
        EnumChatFormatting.YELLOW, lib.DecFormat.short(maxMJPerTick * Config.powerShowMultiplier), Config.powerShowUnits
      )),
      Misc.toLocalF("advgenerators.tooltip.turbine.inertia", EnumChatFormatting.YELLOW + lib.DecFormat.short(inertiaMultiplier))
    )
  }
}

abstract class TileTurbineBase extends TileModule {
  val kind: String = "Turbine"
}