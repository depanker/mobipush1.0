package com.depanker.util

import scala.reflect.ClassTag

/**
 * Created by depankersharma on 02/09/15.
 */
object Debug {

  def getType[T](v: T)(implicit ev: ClassTag[T]) = ev.toString
}
