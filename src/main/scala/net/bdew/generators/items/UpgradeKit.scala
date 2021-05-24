package net.bdew.generators.items

import net.bdew.generators.modules.{BaseController, BaseModule}
import net.bdew.generators.recipes.UpgradeRecipe
import net.bdew.generators.registries.{Items, Recipes}
import net.bdew.lib.PimpVanilla.pimpBlockReader
import net.bdew.lib.multiblock.tile.TileController
import net.minecraft.item.{Item, ItemStack, ItemUseContext}
import net.minecraft.util.math.BlockPos
import net.minecraft.util.{ActionResultType, SoundEvents}
import net.minecraft.world.World
import org.apache.logging.log4j.{LogManager, Logger}

class UpgradeKit extends Item(Items.upgradeKitProps) {
  val log: Logger = LogManager.getLogger

  def findCore(pos: BlockPos, world: World): Option[TileController] = {
    world.getBlockState(pos).getBlock match {
      case x: BaseModule[_] => x.getTE(world, pos).getCore
      case x: BaseController[_] => Some(x.getTE(world, pos))
      case _ => None
    }
  }

  def findBlockToUpgrade(pos: BlockPos, world: World, recipe: UpgradeRecipe): Option[BlockPos] = {
    if (world.getBlockState(pos).getBlock == recipe.from)
      Some(pos)
    else
      findCore(pos, world).flatMap(core => core.getModulePositions(recipe.from).headOption)
  }


  override def onItemUseFirst(stack: ItemStack, ctx: ItemUseContext): ActionResultType = {
    val pos = ctx.getClickedPos
    val world = ctx.getLevel

    if (ctx.getPlayer.isCrouching) return ActionResultType.PASS

    val recipe = Recipes.upgradeType.getAllRecipes(world.getRecipeManager).find(_.item == this).getOrElse({
      log.warn(s"No upgrade recipe found for $getRegistryName")
      return ActionResultType.FAIL
    })

    val target = findBlockToUpgrade(pos, world, recipe).getOrElse(return ActionResultType.PASS)

    if (world.isClientSide) return ActionResultType.SUCCESS

    val oldTile = recipe.from.getTE(world, target)
    val oldConnected = oldTile.connected.value

    oldTile.onBreak()
    oldTile.connected.unset()

    world.setBlock(target, recipe.to.defaultBlockState, 3)
    val newTile = recipe.to.getTE(world, target)

    oldConnected.flatMap(world.getTileSafe[TileController](_)).foreach(controller => {
      newTile.connect(controller)
      controller.modulesChanged = true
    })

    world.playSound(null, pos, SoundEvents.METAL_PLACE, ctx.getPlayer.getSoundSource, 1, 1)

    if (!ctx.getPlayer.abilities.instabuild) {
      stack.shrink(1)
    }

    ActionResultType.CONSUME
  }
}
