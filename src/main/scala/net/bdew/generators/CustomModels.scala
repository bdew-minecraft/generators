package net.bdew.generators

import net.bdew.lib.multiblock.render.MultiblockModelEnhancer
import net.bdew.lib.render.models.EnhancedModelLoader

object CustomModels {
  val multiblockModelLoader = new EnhancedModelLoader(new MultiblockModelEnhancer(GeneratorsResourceProvider))
}


