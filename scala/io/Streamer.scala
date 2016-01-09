package org.sa.lib.io

import java.io.{InputStream, OutputStream}

import scala.reflect.ClassTag

/**
  * Created by SourceCodeBot on 18.07.2015.
  */
class Streamer[A <: InputStream, B <: OutputStream](private[this] var read: Int = 0, val closeIn: Boolean = true, val closeOuts: Boolean = true) {

  def streaming(in: A, outs: Array[B]): Boolean = {
    try {
      val buffer = new Array[Byte](1024)
      Stream.continually(in.read(buffer)).takeWhile(_ != -1).foreach(write(buffer, _, outs))
      close(in, outs)
      true
    } catch {
      case t: Throwable => false
    }
  }

  def close(in: A, outs: Array[B]): Unit = {
    if (this.closeIn) in.close
    if (this.closeOuts) outs.filterNot(_.eq(System.out)).foreach(out => {
      out.flush
      out.close
    })
  }

  def write(buffer: Array[Byte], len: Int, outs: Array[B]): Unit = {
		outs.foreach(out => out.write(buffer, 0, len))
    read += len
  }

  def getRead: Int = this.read

}

object Streamer {

  def apply[A <: InputStream, B <: OutputStream]: Streamer[A, B] = new Streamer[A, B]

  def apply[A <: InputStream, B <: OutputStream](closingIn: Boolean, closingOuts: Boolean)(implicit m: ClassTag[B]): Streamer[A, B] = new Streamer[A, B](0, closingIn, closingOuts)

  def streaming[A <: InputStream, B <: OutputStream](in: A, outs: Array[B], closingOuts: Boolean)(implicit m: ClassTag[B]): Boolean = streaming[A, B](in, outs, closingOuts, false)

  def streaming[A <: InputStream, B <: OutputStream](in: A, outs: Array[B], closingOuts: Boolean, closingIn: Boolean)(implicit m: ClassTag[B]): Boolean = new Streamer(0, closingIn, closingOuts).streaming(in, outs)

  def streaming[A <: InputStream, B <: OutputStream](in: A, out: B)(implicit m: ClassTag[B]): Boolean = streaming[A, B](in, Array(out))

  def streaming[A <: InputStream, B <: OutputStream](in: A, outs: Array[B])(implicit m: ClassTag[B]): Boolean = new Streamer().streaming(in, outs)

  def streaming[A <: InputStream, B <: OutputStream](in: A, out: B, closingOuts: Boolean)(implicit m: ClassTag[B]): Boolean = streaming[A, B](in, out, closingOuts, false)

  def streaming[A <: InputStream, B <: OutputStream](in: A, out: B, closingOuts: Boolean, closingIn: Boolean)(implicit m: ClassTag[B]): Boolean = new Streamer(closeIn = closingIn, closeOuts = closingOuts).streaming(in, Array[B](out))

}


