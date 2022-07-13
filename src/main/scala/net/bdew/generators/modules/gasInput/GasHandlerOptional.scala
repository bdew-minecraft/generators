package net.bdew.generators.modules.gasInput

import mekanism.api.Action
import mekanism.api.chemical.gas.{GasStack, IGasHandler}
import net.minecraft.world.level.Level
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.fluids.capability.IFluidHandler

class GasHandlerOptional(base: () => Option[IFluidHandler], getWorld: () => Level) extends IGasHandler {
  override def getTanks: Int = base().map(_.getTanks).getOrElse(0)
  override def getTankCapacity(tank: Int): Long = base().map(_.getTankCapacity(tank).toLong).getOrElse(0L)

  override def isValid(tank: Int, stack: GasStack): Boolean =
    GasConversion.convert(stack, getWorld()).exists(fs => base().exists(_.isFluidValid(tank, fs)))

  override def getChemicalInTank(tank: Int): GasStack =
    base().map(_.getFluidInTank(tank)).flatMap(GasConversion.convert(_, getWorld())).getOrElse(GasStack.EMPTY)

  override def setChemicalInTank(tank: Int, stack: GasStack): Unit =
    throw new RuntimeException("This should never be called")

  override def insertChemical(tank: Int, stack: GasStack, action: Action): GasStack = {
    val inserted = GasConversion.convert(stack, getWorld()).flatMap(fs =>
      base().map(handler => handler.fill(fs, GasConversion.convertAction(action)))
    ).getOrElse(0)
    if (inserted > 0) {
      val copy = stack.copy()
      copy.shrink(inserted)
      copy
    } else stack
  }

  override def extractChemical(tank: Int, amount: Long, action: Action): GasStack =
    base().map(_.drain(if (amount > Int.MaxValue) Int.MaxValue else amount.toInt, GasConversion.convertAction(action)))
      .flatMap(GasConversion.convert(_, getWorld())).getOrElse(GasStack.EMPTY)
}

object GasHandlerOptional {
  def create(base: () => Option[IFluidHandler], getWorld: () => Level): LazyOptional[IGasHandler] = {
    val handler = new GasHandlerOptional(base, getWorld)
    LazyOptional.of(() => handler)
  }
}