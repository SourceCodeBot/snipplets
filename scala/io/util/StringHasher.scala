package io.util

import java.io.ByteArrayInputStream

import io.Hasher

import scala.util.{Failure, Success, Try}

/**
  * [[io.Hasher]] implementation for hashing string values
  *
  * @author SourceCodeBot <dev@nils-heinemann.de> 11.08.2015.
  */
class StringHasher extends Hasher {

  /**
    * method around the [[getHash]]
    *
    * @param str       [[String]] string value to hash
    * @param algorithm [[scala.Option]] of [[String]] algorithm to take for hashing method
    * @return [[String]] hash value or empty string
    */
  def getStringHash(str: String, algorithm: Option[String] = None): String = {
    val stream = new ByteArrayInputStream(str.getBytes())
    Try(getHash(stream, algorithm)).map {
      case value if stream.available() == 0 =>
        stream.close()
        value

      case value => value
    } match {
      case Success(value) => value
      case Failure(exception) =>
        exception.printStackTrace()
        ""
    }
  }



}

/**
  * facade for [[io.util.StringHasher]]
  */
object StringHasher {
  lazy val instance = new StringHasher

  def apply: StringHasher = instance

  /**
    *
    * @param str [[String]] value to hash
    * @param algorithm [[scala.Option]] algorithm to take for hashing method
    * @return [[String]]
    */
  def getStringHash(str: String, algorithm: Option[String] = None): String = instance.getStringHash(str, algorithm)

}
