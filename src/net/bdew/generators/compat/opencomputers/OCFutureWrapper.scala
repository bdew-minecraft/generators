/*
 * Copyright (c) bdew, 2014 - 2016
 * https://github.com/bdew/generators
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.generators.compat.opencomputers

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}

import li.cil.oc.api.machine.{Arguments, Callback, Context}
import li.cil.oc.api.prefab.AbstractValue
import net.bdew.lib.computers.Result
import net.minecraft.nbt.NBTTagCompound

import scala.concurrent.Future
import scala.util.{Failure, Success}

class OCFutureWrapper(var future: Future[Result]) extends AbstractValue {
  def this() = this(Future.failed(new RuntimeException("Persisted while pending")))

  override def load(nbt: NBTTagCompound): Unit = {
    if (nbt.hasKey("error", 8))
      future = Future.failed(new RuntimeException(nbt.getString("error")))
    else if (nbt.hasKey("value", 7)) {
      try {
        val ois = new ObjectInputStream(new ByteArrayInputStream(nbt.getByteArray("value")))
        future = Future.successful(ois.readObject().asInstanceOf[Result])
      } catch {
        case t: Throwable => future = Future.failed(new RuntimeException("Error when loading: " + t.toString))
      }
    }
  }

  override def save(nbt: NBTTagCompound): Unit = {
    future.value match {
      case Some(Success(x)) =>
        try {
          val bos = new ByteArrayOutputStream()
          val oos = new ObjectOutputStream(bos)
          oos.writeObject(x)
          nbt.setByteArray("value", bos.toByteArray)
        } catch {
          case t: Throwable => nbt.setString("error", "Error when persisting: " + t.toString)
        }
      case Some(Failure(f)) => nbt.setString("error", f.toString)
      case None => nbt.setString("error", "Persisted while pending")
    }
  }

  @Callback(direct = true, doc = "Returns whether the future has already been completed with a value or an error")
  def isCompleted(context: Context, arguments: Arguments): Array[AnyRef] = Array(Boolean.box(future.isCompleted))

  @Callback(direct = true, doc = "Returns whether the future has already been completed with a value")
  def isSuccess(context: Context, arguments: Arguments): Array[AnyRef] = Array(Boolean.box(future.isCompleted && future.value.get.isSuccess))

  @Callback(direct = true, doc = "Returns whether the future has already been completed with an error")
  def isFailure(context: Context, arguments: Arguments): Array[AnyRef] = Array(Boolean.box(future.isCompleted && future.value.get.isFailure))

  @Callback(direct = true)
  def get(context: Context, arguments: Arguments): Array[AnyRef] = {
    future.value match {
      case Some(Success(x)) => OCResultConverter.wrap(x)
      case Some(Failure(f)) => throw f
      case None => throw new RuntimeException("Future not completed")
    }
  }
}
