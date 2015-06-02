/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.items

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.bdew.generators.config.TurbineMaterial
import net.bdew.lib.items.NamedItem
import net.minecraft.client.renderer.texture.IIconRegister

class TurbineItem(val material: TurbineMaterial, val kind: String) extends NamedItem {
  override def name = "Turbine" + kind + material.name

  setUnlocalizedName("advgenerators." + name)

  @SideOnly(Side.CLIENT)
  override def registerIcons(reg: IIconRegister): Unit = {
    itemIcon = reg.registerIcon("advgenerators:turbine/%s/%s".format(material.name.toLowerCase, kind.toLowerCase))
  }
}
