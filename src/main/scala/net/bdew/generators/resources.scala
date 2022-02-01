package net.bdew.generators

import net.bdew.lib.Text
import net.bdew.lib.gui.{Color, ScaledResourceLocation, Sprite, Texture}
import net.bdew.lib.multiblock.ResourceProvider
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation

object Textures {
  val sheet = new ScaledResourceLocation(Generators.ModId, "textures/gui/widgets.png")
  val tankOverlay: Sprite = Texture(sheet, 10, 0, 9, 58)
  val tankOverlaySmaller: Sprite = Texture(sheet, 137, 54, 10, 35)
  val powerFill: Sprite = Texture(sheet, 0, 0, 9, 58)
  val slotSelect: Sprite = Texture(sheet, 20, 0, 18, 18)

  object Button16 {
    val base: Sprite = Texture(sheet, 20, 18, 16, 16)
    val hover: Sprite = Texture(sheet, 36, 18, 16, 16)
    val red: Sprite = Texture(sheet, 52, 18, 16, 16)

    val rsOn: Sprite = Texture(sheet, 37, 35, 14, 14)
    val rsOff: Sprite = Texture(sheet, 21, 35, 14, 14)
    val enabled: Sprite = Texture(sheet, 53, 35, 14, 14)
    val disabled: Sprite = Texture(sheet, 69, 35, 14, 14)
    val wrench: Sprite = Texture(sheet, 85, 35, 14, 14)
  }

  object Icons {
    val turbine: Sprite = Texture(sheet, 0, 90, 32, 32)
    val out: Sprite = Texture(sheet, 32, 90, 16, 16)
    val peak: Sprite = Texture(sheet, 32, 106, 16, 16)
    val power: Sprite = Texture(sheet, 48, 90, 16, 16)
    val fluid: Sprite = Texture(sheet, 64, 90, 32, 32)
    val temperature: Sprite = Texture(sheet, 96, 90, 32, 32)
    val heatExchange: Sprite = Texture(sheet, 48, 106, 16, 16)
    val heatLoss: Sprite = Texture(sheet, 0, 122, 16, 16)
    val speed: Sprite = Texture(sheet, 32, 122, 32, 32)
    val fire: Sprite = Texture(sheet, 69, 19, 14, 14)
  }

  def progress(width: Int): Sprite = Texture(sheet, (136 - width).toFloat, 74, width.toFloat, 16)
}

object GeneratorsResourceProvider extends ResourceProvider {
  override def edge = new ResourceLocation("advgenerators:blocks/connected/edge")
  override def arrow = new ResourceLocation("advgenerators:blocks/connected/arrow")
  override def output = new ResourceLocation("advgenerators:blocks/connected/output")
  override def disabled = new ResourceLocation("advgenerators:blocks/connected/disabled")

  override def btRsOff: Texture = Textures.Button16.rsOff
  override def btRsOn: Texture = Textures.Button16.rsOn
  override def btDisabled: Texture = Textures.Button16.disabled
  override def btEnabled: Texture = Textures.Button16.enabled

  override def btBase: Texture = Textures.Button16.base
  override def btHover: Texture = Textures.Button16.hover

  override val outputColors = Map(
    0 -> Color(1F, 0F, 0F),
    1 -> Color(0F, 1F, 0F),
    2 -> Color(0F, 0F, 1F),
    3 -> Color(1F, 1F, 0F),
    4 -> Color(0F, 1F, 1F),
    5 -> Color(1F, 0F, 1F)
  )
  override val unlocalizedOutputName: Map[Int, String] = (outputColors.keys map (n => n -> "advgenerators.output.%d".format(n))).toMap

  override def getModuleName(s: String): MutableComponent = Text.translate("advgenerators.module." + s + ".name")
  override def getMachineName(s: String): MutableComponent = Text.translate("block.advgenerators." + s)
}