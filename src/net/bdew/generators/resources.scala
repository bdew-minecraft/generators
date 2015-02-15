/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators

import net.bdew.lib.gui.{Color, ScaledResourceLocation, Texture}
import net.bdew.lib.multiblock.ResourceProvider
import net.bdew.lib.render.IconPreloader

object IconCache extends IconPreloader(0) {
  val edgeIcon = TextureLoc("advgenerators:connected/edge")
  val output = TextureLoc("advgenerators:connected/output")
  val disabled = TextureLoc("advgenerators:connected/disabled")
  val arTop = TextureLoc("advgenerators:connected/artop")
  val arRight = TextureLoc("advgenerators:connected/arright")
  val arBottom = TextureLoc("advgenerators:connected/arbottom")
  val arLeft = TextureLoc("advgenerators:connected/arleft")
}

object Textures {
  val sheet = new ScaledResourceLocation(Generators.modId, "textures/gui/widgets.png")
  val tankOverlay = Texture(sheet, 10, 0, 9, 58)
  val powerFill = Texture(sheet, 0, 0, 9, 58)
  val slotSelect = Texture(sheet, 20, 0, 18, 18)

  object Button16 {
    val base = Texture(sheet, 20, 18, 16, 16)
    val hover = Texture(sheet, 36, 18, 16, 16)
    val red = Texture(sheet, 52, 18, 16, 16)

    val rsOn = Texture(sheet, 37, 35, 14, 14)
    val rsOff = Texture(sheet, 21, 35, 14, 14)
    val enabled = Texture(sheet, 53, 35, 14, 14)
    val disabled = Texture(sheet, 69, 35, 14, 14)
    val wrench = Texture(sheet, 85, 35, 14, 14)
  }

  object Icons {
    val turbine = Texture(sheet, 0, 90, 32, 32)
    val out = Texture(sheet, 32, 90, 16, 16)
    val peak = Texture(sheet, 32, 106, 16, 16)
    val power = Texture(sheet, 48, 90, 16, 16)
    val fluid = Texture(sheet, 64, 90, 32, 32)
    val temperature = Texture(sheet, 96, 90, 32, 32)
    val heatExchange = Texture(sheet, 48, 106, 16, 16)
    val heatLoss = Texture(sheet, 0, 122, 16, 16)
    val speed = Texture(sheet, 32, 122, 32, 32)
    val fire = Texture(sheet, 69, 19, 14, 14)
  }

  def progress(width: Int) = Texture(sheet, 136 - width, 74, width, 16)
}

object GeneratorsResourceProvider extends ResourceProvider {
  override def edge = IconCache.edgeIcon
  override def output = IconCache.output
  override def disabled = IconCache.disabled

  override def arrowBottom = IconCache.arBottom
  override def arrowRight = IconCache.arRight
  override def arrowTop = IconCache.arTop
  override def arrowLeft = IconCache.arLeft

  override def btRsOff = Textures.Button16.rsOff
  override def btRsOn = Textures.Button16.rsOn
  override def btDisabled = Textures.Button16.disabled
  override def btEnabled = Textures.Button16.enabled

  override def btBase = Textures.Button16.base
  override def btHover = Textures.Button16.hover

  override val outputColors = Map(
    0 -> Color(1F, 0F, 0F),
    1 -> Color(0F, 1F, 0F),
    2 -> Color(0F, 0F, 1F),
    3 -> Color(1F, 1F, 0F),
    4 -> Color(0F, 1F, 1F),
    5 -> Color(1F, 0F, 1F)
  )
  override val unlocalizedOutputName = (outputColors.keys map (n => n -> "advgenerators.output.%d".format(n))).toMap

  override def getModuleName(s: String) = "advgenerators.module." + s + ".name"
}