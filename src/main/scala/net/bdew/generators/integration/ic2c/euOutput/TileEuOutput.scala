package net.bdew.generators.integration.ic2c.euOutput

import ic2.api.energy.EnergyNet
import ic2.api.energy.tile.{IEnergyAcceptor, IEnergySource}
import net.bdew.generators.config.Config
import net.bdew.generators.controllers.CIPowerAccess
import net.bdew.generators.integration.ic2c.IC2Tier
import net.bdew.generators.registries.Modules
import net.bdew.lib.multiblock.ModuleType
import net.bdew.lib.multiblock.data.OutputConfigPower
import net.bdew.lib.multiblock.interact.CIPowerOutput
import net.bdew.lib.multiblock.tile.{RSControllableOutput, TileOutput, TileOutputTracker}
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class TileEuOutput(teType: BlockEntityType[_], pos: BlockPos, state: BlockState, tier: IC2Tier) extends TileOutput[OutputConfigPower](teType, pos, state) with RSControllableOutput with IEnergySource {
  val kind: ModuleType = Modules.powerOutput

  override def getCore: Option[CIPowerOutput with CIPowerAccess] = getCoreAs[CIPowerOutput with CIPowerAccess]

  override val outputConfigType: Class[OutputConfigPower] = classOf[OutputConfigPower]
  override def makeCfgObject(face: Direction) = new OutputConfigPower("eu")

  val outputTracker: TileOutputTracker[OutputConfigPower] = new TileOutputTracker(this, _.updateAvg(_))

  private var addedToNetwork = false


  override def setRemoved(): Unit = {
    if (addedToNetwork) {
      EnergyNet.INSTANCE.removeTile(this)
      addedToNetwork = false
    }
    super.setRemoved()
  }

  override def onChunkUnloaded(): Unit = {
    if (addedToNetwork) {
      EnergyNet.INSTANCE.removeTile(this)
      addedToNetwork = false
    }
    super.onChunkUnloaded()
  }

  //  override def clearRemoved(): Unit = {
  //    super.clearRemoved()
  //    if (!this.getWorldObject.isClientSide && !addedToNetwork) {
  //      EnergyNet.INSTANCE.addTile(this)
  //      addedToNetwork = true
  //    }
  //  }

  override def onLoad(): Unit = {
    super.onLoad()
    if (!this.getWorldObject.isClientSide && !addedToNetwork) {
      EnergyNet.INSTANCE.addTile(this)
      addedToNetwork = true
    }
  }

  def ratio: Double = Config.Integration.IC2.ratio()
  def facing: Direction = getBlockState.getBlock.asInstanceOf[BlockEuOutput].getFacing(getWorldObj.getBlockState(getPosition))

  override def getSourceTier: Int = tier.number
  override def getMaxEnergyOutput: Int = tier.maxPower

  override def getProvidedEnergy: Int = {
    val out = for {
      core <- getCore
      cfg <- getCfg(facing) if checkCanOutput(cfg)
    } yield (core.power.stored * ratio).floor.toInt
    Math.min(out.getOrElse(0), tier.maxPower)
  }

  override def consumeEnergy(i: Int): Unit = {
    getCore.foreach(c => c.power.extract((i.toDouble / ratio).toFloat, false))
    outputTracker.track(facing, i.toFloat)
  }

  override def canEmitEnergy(iEnergyAcceptor: IEnergyAcceptor, direction: Direction): Boolean = {
    getCore.isDefined && facing == direction
  }

  override def canConnectToFace(d: Direction): Boolean =
    d == facing

  def facingChanged(): Unit = {
    doRescanFaces()
    if (addedToNetwork) {
      EnergyNet.INSTANCE.updateTile(this)
    }
  }

  override def doOutput(face: Direction, cfg: OutputConfigPower): Unit = {}
}
