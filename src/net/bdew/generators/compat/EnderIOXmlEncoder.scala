/*
 * Copyright (c) bdew, 2015
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.compat

import net.bdew.generators.config.loader.{Loader, RsEnderIOSagMill, RsEnderIOSmelt, StackRefCount}
import net.bdew.lib.recipes.{StackItem, StackMacro, StackOreDict, StackRef}
import net.minecraftforge.oredict.OreDictionary

import scala.xml.{Attribute, Elem, Null}

class EnderIOXmlEncoder(loader: Loader) {
  val IDAlloySmelter = "recipe:alloysmelter"
  val IDSagMill = "recipe:sagmill"

  val recipeIdNum = Iterator.from(1)

  def encodeItemStackCount(v: StackRefCount) = encodeItemStack(v.stack, v.count)

  def encodeItemStack(stack: StackRef, count: Int = 1): Elem = {
    stack match {
      case StackOreDict(id) =>
          <itemStack oreDictionary={id} number={count.toString}/>

      case StackItem(mod, item, meta) =>
          <itemStack modID={mod} itemName={item} itemMeta={if (meta == OreDictionary.WILDCARD_VALUE) "*" else meta.toString}/>

      case StackMacro(ch) => encodeItemStack(loader.currCharMap(ch), count)

      case _ => sys.error("Unsupported itemstack: " + stack)
    }
  }

  private def addExp(elem: Elem, xp: Double) = {
    elem.attributes.append(Attribute("", "exp", xp.toString, Null))
    elem
  }

  def encodeSmelterRecipe(recipe: RsEnderIOSmelt) = {
    <recipeGroup name="AdvancedGenerators">
      <recipe name={"recipe" + recipeIdNum.next()} energyCost={recipe.energy.toString}>
        <input>
          {recipe.inputs.map(encodeItemStackCount)}
        </input>
        <output>
          {addExp(encodeItemStackCount(recipe.output), recipe.xp)}
        </output>
      </recipe>
    </recipeGroup>
  }

  def encodeSagMill(recipe: RsEnderIOSagMill) = {
    <recipeGroup name="AdvancedGenerators">
      <recipe name={"recipe" + recipeIdNum.next()} energyCost={recipe.energy.toString}>
        <input>
          {encodeItemStack(recipe.input)}
        </input>
        <output>
          {recipe.outputs.map(encodeItemStackCount)}
        </output>
      </recipe>
      // Add exclude if needed
      {if (!recipe.bonus)
      <grindingBalls>
        <excludes>
          {encodeItemStack(recipe.input)}
        </excludes>
      </grindingBalls>}
    </recipeGroup>
  }
}
