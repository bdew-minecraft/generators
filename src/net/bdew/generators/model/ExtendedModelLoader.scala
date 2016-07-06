/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.model

import net.bdew.lib.render.models.ModelEnhancer
import net.minecraft.client.resources.IResourceManager
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.{ICustomModelLoader, ModelLoaderRegistry}

object ExtendedModelLoader extends ICustomModelLoader {
  override def accepts(modelLocation: ResourceLocation) =
    modelLocation.getResourceDomain.equals("advgenerators") && modelLocation.getResourcePath.endsWith(".extended")

  def wrap(model: String, enhancer: ModelEnhancer) =
    enhancer.wrap(ModelLoaderRegistry.getModel(new ResourceLocation(model)))

  override def loadModel(modelLocation: ResourceLocation) =
    modelLocation.getResourcePath match {
      case "models/block/sided_multiblock.extended" => wrap("minecraft:block/cube_bottom_top", GeneratorsMultiblockEnhancer)
      case "models/block/sided_multiblock_rotated.extended" => wrap("advgenerators:block/rotated", GeneratorsMultiblockEnhancer)
      case _ => null
    }

  override def onResourceManagerReload(resourceManager: IResourceManager) = {}
}
