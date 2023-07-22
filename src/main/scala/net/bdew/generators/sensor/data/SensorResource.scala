package net.bdew.generators.sensor.data

import net.bdew.generators.sensor.{CastSensor, Icons}
import net.bdew.lib.gui.{DrawTarget, Rect}
import net.bdew.lib.resource.DataSlotResource
import net.bdew.lib.sensors.GenericSensorParameter
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.world.item.{ItemStack, Items}
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraftforge.api.distmarker.{Dist, OnlyIn}

import scala.reflect.ClassTag

case class SensorResource[T: ClassTag](uid: String, iconName: String, accessor: T => DataSlotResource) extends CastSensor[T] with Icons.Loader {
  override val parameters = Vector(
    ParameterFill.empty,
    ParameterResource.solid,
    ParameterResource.fluid,
    ParameterFill.gt5,
    ParameterFill.gt25,
    ParameterFill.gt50,
    ParameterFill.gt75,
    ParameterFill.gt95,
    ParameterFill.nonFull,
    ParameterFill.full
  )

  @OnlyIn(Dist.CLIENT)
  override def drawParameter(graphics: GuiGraphics, rect: Rect, target: DrawTarget, obj: BlockEntity, param: GenericSensorParameter): Unit =
    param match {
      case ParameterResource.solid =>
        target.drawItem(graphics, new ItemStack(Items.COBBLESTONE), rect.origin)
      case ParameterResource.fluid =>
        target.drawItem(graphics, new ItemStack(Items.WATER_BUCKET), rect.origin)
      case _ => super.drawParameter(graphics, rect, target, obj, param)
    }

  override def getResultTyped(param: GenericSensorParameter, te: T): Boolean = (param, te) match {
    case (x: ParameterResource, y: T) =>
      x.test(accessor(y))

    case (x: ParameterFill, y: T) =>
      val ds = accessor(y)
      ds.resource map { resource =>
        x.test(resource.amount, ds.getEffectiveCapacity)
      } getOrElse {
        x.test(0, ds.capacity)
      }
    case _ => false
  }
}
