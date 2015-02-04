/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.exchanger

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.bdew.generators.modules.BaseModule
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.util.IIcon
import net.minecraftforge.common.util.ForgeDirection

object BlockExchanger extends BaseModule("HeatExchanger", "HeatExchanger", classOf[TileExchanger])