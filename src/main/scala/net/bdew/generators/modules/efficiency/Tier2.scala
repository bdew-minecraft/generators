package net.bdew.generators.modules.efficiency

import net.bdew.generators.config.Config
import net.bdew.generators.controllers.turbine.TileFuelTurbineController
import net.bdew.generators.modules.BaseModule
import net.bdew.generators.registries.{Blocks, Machines, Modules}
import net.bdew.lib.PimpVanilla._
import net.bdew.lib.Text
import net.bdew.lib.Text.pimpTextComponent
import net.bdew.lib.multiblock.ModuleType
import net.bdew.lib.multiblock.item.ModuleBlockItem
import net.bdew.lib.multiblock.tile.TileModule
import net.bdew.lib.tile.TileExtended
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.ITextComponent
import net.minecraft.world.World

class BlockEfficiencyUpgradeTier2 extends BaseModule[TileEfficiencyUpgradeTier2](Modules.efficiencyUpgradeTier2)

class BlockItemEfficiencyUpgradeTier2(block: BlockEfficiencyUpgradeTier2) extends ModuleBlockItem(block, Blocks.defaultItemProps, Machines) {
  override def getTooltip(stack: ItemStack, world: World, flags: ITooltipFlag): List[ITextComponent] =
    Text.translate("advgenerators.tooltip.efficiency", Text.string("%.0f%%".format(Config.FuelTurbine.fuelEfficiencyTier2() * 100)).setColor(Text.Color.YELLOW)) +:
      Text.translate("advgenerators.tooltip.efficiency.req") +:
      super.getTooltip(stack, world, flags)
}

class TileEfficiencyUpgradeTier2(teType: TileEntityType[_]) extends TileExtended(teType) with TileModule {
  val kind: ModuleType = Modules.efficiencyUpgradeTier2

  override def canConnectToCore(br: BlockPos): Boolean =
    super.canConnectToCore(br) && getLevel.getTileSafe[TileFuelTurbineController](br).exists(controller =>
      controller.getNumOfModules(Modules.efficiencyUpgradeTier1) == 1
    )
}