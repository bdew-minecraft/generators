package net.bdew.generators.modules.fluidOutputSelect

import net.bdew.generators.registries.Modules
import net.bdew.lib.Misc
import net.bdew.lib.capabilities.helpers._
import net.bdew.lib.capabilities.helpers.fluid._
import net.bdew.lib.capabilities.{Capabilities, SidedCapHandler}
import net.bdew.lib.multiblock.ModuleType
import net.bdew.lib.multiblock.data.OutputConfigFluidSlots
import net.bdew.lib.multiblock.interact.CIFluidOutputSelect
import net.bdew.lib.multiblock.misc.TileForcedOutput
import net.bdew.lib.multiblock.tile.{RSControllableOutput, TileOutput, TileOutputTracker}
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.fluids.capability.IFluidHandler

class TileFluidOutputSelect(teType: BlockEntityType[_], pos: BlockPos, state: BlockState) extends TileOutput[OutputConfigFluidSlots](teType, pos, state) with TileForcedOutput with RSControllableOutput {
  override val kind: ModuleType = Modules.fluidOutput

  override def getCore: Option[CIFluidOutputSelect] = getCoreAs[CIFluidOutputSelect]
  override val outputConfigType: Class[OutputConfigFluidSlots] = classOf[OutputConfigFluidSlots]

  val outputTracker: TileOutputTracker[OutputConfigFluidSlots] = new TileOutputTracker(this, _.updateAvg(_))

  def outputSourceForSide(side: Direction): Option[IFluidHandler] =
    getCore.flatMap(core =>
      getCfg(side).flatMap(cfg =>
        if (checkCanOutput(cfg)) {
          Misc.asInstanceOpt(cfg.slot, classOf[core.outputSlotsDef.Slot]).map(slot =>
            core.fluidOutputForSlot(slot)
          )
        } else None
      )
    )

  val fluidHandlers = new SidedCapHandler[IFluidHandler](side => new FluidDrainMonitor(
    new FluidHandlerOptional(() => outputSourceForSide(side)),
    s => outputTracker.track(side, s.getAmount.toFloat)
  ), allowNull = false)

  //noinspection ComparingUnrelatedTypes
  override def getCapability[T](cap: Capability[T], side: Direction): LazyOptional[T] = {
    if (cap == Capabilities.CAP_FLUID_HANDLER)
      fluidHandlers.get(side).cast()
    else
      super.getCapability(cap, side)
  }

  override def invalidateCaps(): Unit = {
    super.invalidateCaps()
    fluidHandlers.invalidate()
  }

  def getTargetFluidHandler(d: Direction): Option[IFluidHandler] = {
    val tile = getLevel.getBlockEntity(getBlockPos.offset(d.getNormal))
    if (tile == null) return None
    val cap = tile.getCapability(Capabilities.CAP_FLUID_HANDLER, d.getOpposite)
    if (cap.isPresent)
      Option(cap.orElseGet(() => null))
    else
      None
  }

  override def canConnectToFace(d: Direction): Boolean =
    getCore.isDefined && (forcedSides(d) || getTargetFluidHandler(d).isDefined)

  override def makeCfgObject(face: Direction) = new OutputConfigFluidSlots(getCore.get.outputSlotsDef)

  override def doOutput(face: Direction, cfg: OutputConfigFluidSlots): Unit = {
    for {
      source <- outputSourceForSide(face) if checkCanOutput(cfg)
      target <- getTargetFluidHandler(face)
    } {
      val filled = FluidHelper.pushFluid(source, target)
      if (!filled.isEmpty) outputTracker.track(face, filled.getAmount.toFloat)
    }
  }
}
