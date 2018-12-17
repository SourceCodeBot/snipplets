package io.util

import java.io.{File, FileInputStream}

import io.Hasher

import scala.util.{Failure, Success, Try}


/**
  *
  * Utility to Hash a [[java.io.File]] with specified algorithm
  * extends [[io.Hasher]]
  *
  * @author SourceCodeBot <dev@nils-heinemann.de>
  */
class FileHasher extends Hasher {

  /**
    * [[File]] wrapper of [[io.Hasher]]
    *
    * @param file      [[java.io.File]] file to hash
    * @param algorithm [[scala.Option]] algorithm to take for hashing method
    * @return [[String]] hash or empty string
    */
  def getFileHash(file: File, algorithm: Option[String]): String = {
    val stream = new FileInputStream(file)
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
  * facade for [[io.util.FileHasher]]
  * provide unique instance of [[io.util.FileHasher]]
  */
object FileHasher {

  private[this] lazy val _instance = new FileHasher

  def apply: FileHasher = _instance

  /**
    * @param file      [[java.io.File]] file to hash
    * @param algorithm [[scala.Option]] algorithm to take for hashing method
    * @return [[String]] hash or empty string
    */
  def getFileHash(file: File, algorithm: Option[String] = None): String = _instance.getFileHash(file, algorithm)

}