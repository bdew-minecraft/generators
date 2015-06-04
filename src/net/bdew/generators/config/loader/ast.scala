/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.config.loader

import net.bdew.lib.recipes.{CraftingStatement, RecipeStatement, StackRef}

case class RsSetContainer(item: StackRef, cont: StackRef) extends RecipeStatement

case class StackRefCount(stack: StackRef, count: Int)

case class RsEnderIOSmelt(inputs: List[StackRefCount], output: StackRefCount, xp: Double, energy: Int) extends CraftingStatement {
  override val result = output.stack
}

case class RsEnderIOSagMill(input: StackRef, outputs: List[StackRefCount], energy: Int, bonus: Boolean) extends RecipeStatement

case class RsTESmelter(input1: StackRefCount, input2: Option[StackRefCount], output1: StackRefCount, output2: Option[StackRefCount], energy: Int, secondaryChance: Int) extends RecipeStatement

abstract class ResKindRef

case class ResKindFluid(name: String) extends ResKindRef

case class ResKindItem(spec: StackRef) extends ResKindRef

case class ResourceRef(res: ResKindRef, amount: Double)

case class RsTurbineFuel(fluid: String, value: Float) extends RecipeStatement

case class RsTurbineBlacklist(fluid: String) extends RecipeStatement

case class RsExchangerHeat(input: ResourceRef, output: ResourceRef, heat: Double) extends RecipeStatement

case class RsExchangerCool(input: ResourceRef, output: ResourceRef, heat: Double) extends RecipeStatement

case class RsExchangerBlacklist(res: ResKindRef) extends RecipeStatement

abstract class CarbonValue

case class CarbonValueSpecified(v: Int) extends CarbonValue

case class CarbonValueDefault() extends CarbonValue

case class CarbonValueBlacklist() extends CarbonValue

case class RsCarbonValue(stack: StackRef, value: CarbonValue) extends RecipeStatement
