package scala.coroutines






/* Coroutine._1 specializations */

abstract class _1$spec$S[@specialized(Short, Char, Int, Float, Long, Double) S]
extends Coroutine._1[Short, S] {
  def apply(a0: Short): S
  def $call(a0: Short): Coroutine[S]
  def $push(c: Coroutine[S], a0: Short): Unit
}


abstract class _1$spec$C[@specialized(Short, Char, Int, Float, Long, Double) S]
extends Coroutine._1[Char, S] {
  def apply(a0: Char): S
  def $call(a0: Char): Coroutine[S]
  def $push(c: Coroutine[S], a0: Char): Unit
}


abstract class _1$spec$I[@specialized(Short, Char, Int, Float, Long, Double) S]
extends Coroutine._1[Int, S] {
  def apply(a0: Int): S
  def $call(a0: Int): Coroutine[S]
  def $push(c: Coroutine[S], a0: Int): Unit
}


abstract class _1$spec$F[@specialized(Short, Char, Int, Float, Long, Double) S]
extends Coroutine._1[Float, S] {
  def apply(a0: Float): S
  def $call(a0: Float): Coroutine[S]
  def $push(c: Coroutine[S], a0: Float): Unit
}


abstract class _1$spec$J[@specialized(Short, Char, Int, Float, Long, Double) S]
extends Coroutine._1[Long, S] {
  def apply(a0: Long): S
  def $call(a0: Long): Coroutine[S]
  def $push(c: Coroutine[S], a0: Long): Unit
}


abstract class _1$spec$D[@specialized(Short, Char, Int, Float, Long, Double) S]
extends Coroutine._1[Double, S] {
  def apply(a0: Double): S
  def $call(a0: Double): Coroutine[S]
  def $push(c: Coroutine[S], a0: Double): Unit
}

abstract class _1$spec$L[T0, @specialized(Short, Char, Int, Float, Long, Double) S]
extends Coroutine._1[T0, S] {
  def apply(a0: T0): S
  def $call(a0: T0): Coroutine[S]
  def $push(c: Coroutine[S], a0: T0): Unit
}

/* Coroutine._2 specializations. */

abstract class _2$spec$II[@specialized(Int, Long, Double) S]
extends Coroutine._2[Int, Int, S] {
  def apply(a0: Int, a1: Int): S
  def $call(a0: Int, a1: Int): Coroutine[S]
  def $push(c: Coroutine[S], a0: Int, a1: Int): Unit
}

abstract class _2$spec$JI[@specialized(Int, Long, Double) S]
extends Coroutine._2[Long, Int, S] {
  def apply(a0: Long, a1: Int): S
  def $call(a0: Long, a1: Int): Coroutine[S]
  def $push(c: Coroutine[S], a0: Long, a1: Int): Unit
}

abstract class _2$spec$DI[@specialized(Int, Long, Double) S]
extends Coroutine._2[Double, Int, S] {
  def apply(a0: Double, a1: Int): S
  def $call(a0: Double, a1: Int): Coroutine[S]
  def $push(c: Coroutine[S], a0: Double, a1: Int): Unit
}

abstract class _2$spec$LI[T0, @specialized(Int, Long, Double) S]
extends Coroutine._2[T0, Int, S] {
  def apply(a0: T0, a1: Int): S
  def $call(a0: T0, a1: Int): Coroutine[S]
  def $push(c: Coroutine[S], a0: T0, a1: Int): Unit
}

abstract class _2$spec$IJ[@specialized(Int, Long, Double) S]
extends Coroutine._2[Int, Long, S] {
  def apply(a0: Int, a1: Long): S
  def $call(a0: Int, a1: Long): Coroutine[S]
  def $push(c: Coroutine[S], a0: Int, a1: Long): Unit
}

abstract class _2$spec$JJ[@specialized(Int, Long, Double) S]
extends Coroutine._2[Long, Long, S] {
  def apply(a0: Long, a1: Long): S
  def $call(a0: Long, a1: Long): Coroutine[S]
  def $push(c: Coroutine[S], a0: Long, a1: Long): Unit
}

abstract class _2$spec$DJ[@specialized(Int, Long, Double) S]
extends Coroutine._2[Double, Long, S] {
  def apply(a0: Double, a1: Long): S
  def $call(a0: Double, a1: Long): Coroutine[S]
  def $push(c: Coroutine[S], a0: Double, a1: Long): Unit
}

abstract class _2$spec$LJ[T0, @specialized(Int, Long, Double) S]
extends Coroutine._2[T0, Long, S] {
  def apply(a0: T0, a1: Long): S
  def $call(a0: T0, a1: Long): Coroutine[S]
  def $push(c: Coroutine[S], a0: T0, a1: Long): Unit
}

abstract class _2$spec$ID[@specialized(Int, Long, Double) S]
extends Coroutine._2[Int, Double, S] {
  def apply(a0: Int, a1: Double): S
  def $call(a0: Int, a1: Double): Coroutine[S]
  def $push(c: Coroutine[S], a0: Int, a1: Double): Unit
}

abstract class _2$spec$JD[@specialized(Int, Long, Double) S]
extends Coroutine._2[Long, Double, S] {
  def apply(a0: Long, a1: Double): S
  def $call(a0: Long, a1: Double): Coroutine[S]
  def $push(c: Coroutine[S], a0: Long, a1: Double): Unit
}

abstract class _2$spec$DD[@specialized(Int, Long, Double) S]
extends Coroutine._2[Double, Double, S] {
  def apply(a0: Double, a1: Double): S
  def $call(a0: Double, a1: Double): Coroutine[S]
  def $push(c: Coroutine[S], a0: Double, a1: Double): Unit
}

abstract class _2$spec$LD[T0, @specialized(Int, Long, Double) S]
extends Coroutine._2[T0, Double, S] {
  def apply(a0: T0, a1: Double): S
  def $call(a0: T0, a1: Double): Coroutine[S]
  def $push(c: Coroutine[S], a0: T0, a1: Double): Unit
}

abstract class _2$spec$IL[T1, @specialized(Int, Long, Double) S]
extends Coroutine._2[Int, T1, S] {
  def apply(a0: Int, a1: T1): S
  def $call(a0: Int, a1: T1): Coroutine[S]
  def $push(c: Coroutine[S], a0: Int, a1: T1): Unit
}

abstract class _2$spec$JL[T1, @specialized(Int, Long, Double) S]
extends Coroutine._2[Long, T1, S] {
  def apply(a0: Long, a1: T1): S
  def $call(a0: Long, a1: T1): Coroutine[S]
  def $push(c: Coroutine[S], a0: Long, a1: T1): Unit
}

abstract class _2$spec$DL[T1, @specialized(Int, Long, Double) S]
extends Coroutine._2[Double, T1, S] {
  def apply(a0: Double, a1: T1): S
  def $call(a0: Double, a1: T1): Coroutine[S]
  def $push(c: Coroutine[S], a0: Double, a1: T1): Unit
}

abstract class _2$spec$LL[T0, T1, @specialized(Int, Long, Double) S]
extends Coroutine._2[T0, T1, S] {
  def apply(a0: T0, a1: T1): S
  def $call(a0: T0, a1: T1): Coroutine[S]
  def $push(c: Coroutine[S], a0: T0, a1: T1): Unit
}