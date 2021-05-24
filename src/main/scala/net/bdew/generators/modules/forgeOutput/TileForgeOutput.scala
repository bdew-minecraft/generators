package net.bdew.generators.modules.forgeOutput

import net.bdew.generators.registries.Modules
import net.bdew.lib.capabilities.helpers.EnergyHelper
import net.bdew.lib.capabilities.helpers.energy.{EnergyDrainMonitor, EnergyHandlerOptional}
import net.bdew.lib.capabilities.{Capabilities, SidedCapHandler}
import net.bdew.lib.multiblock.ModuleType
import net.bdew.lib.multiblock.data.OutputConfigPower
import net.bdew.lib.multiblock.interact.CIPowerOutput
import net.bdew.lib.multiblock.misc.TileForcedOutput
import net.bdew.lib.multiblock.tile.{RSControllableOutput, TileOutput, TileOutputTracker}
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.energy.IEnergyStorage

class TileForgeOutput(teType: TileEntityType[_]) extends TileOutput[OutputConfigPower](teType) with RSControllableOutput with TileForcedOutput {
  val kind: ModuleType = Modules.powerOutput

  override def getCore: Option[CIPowerOutput] = getCoreAs[CIPowerOutput]

  override val outputConfigType: Class[OutputConfigPower] = classOf[OutputConfigPower]
  override def makeCfgObject(face: Direction) = new OutputConfigPower("fe")

  val outputTracker: TileOutputTracker[OutputConfigPower] = new TileOutputTracker(this, _.updateAvg(_))

  val powerHandlers = new SidedCapHandler[IEnergyStorage](side => new EnergyDrainMonitor(
    new EnergyHandlerOptional(() => getCore.map(_.powerOutput)),
    x => outputTracker.track(side, x.toFloat)
  ), allowNull = false)

  //noinspection ComparingUnrelatedTypes
  override def getCapability[T](cap: Capability[T], side: Direction): LazyOptional[T] = {
    if (cap == Capabilities.CAP_ENERGY_HANDLER)
      powerHandlers.get(side).cast()
    else
      super.getCapability(cap, side)
  }

  override def invalidateCaps(): Unit = {
    super.invalidateCaps()
    powerHandlers.invalidate()
  }

  def getTargetPowerHandler(d: Direction): Option[IEnergyStorage] = {
    val tile = getLevel.getBlockEntity(getBlockPos.offset(d.getNormal))
    if (tile == null) return None
    val cap = tile.getCapability(Capabilities.CAP_ENERGY_HANDLER, d.getOpposite)
    if (cap.isPresent)
      Option(cap.orElseGet(() => null))
    else
      None
  }

  override def canConnectToFace(d: Direction): Boolean =
    forcedSides(d) || getTargetPowerHandler(d).isDefined

  override def doOutput(face: Direction, cfg: OutputConfigPower): Unit = {
    for {
      core <- getCore if checkCanOutput(cfg)
      target <- getTargetPowerHandler(face)
    } {
      val moved = EnergyHelper.pushEnergy(core.powerOutput, target, false)
      outputTracker.track(face, moved.toFloat)
    }
  }
}
