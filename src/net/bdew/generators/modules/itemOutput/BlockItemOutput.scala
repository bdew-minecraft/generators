/*
 * Copyright (c) bdew, 2014
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.modules.itemOutput

import net.bdew.generators.modules.BaseModule
import net.bdew.lib.multiblock.block.BlockOutput

object BlockItemOutput extends BaseModule("ItemOutput", "ItemOutput", classOf[TileItemOutput]) with BlockOutput[TileItemOutput]