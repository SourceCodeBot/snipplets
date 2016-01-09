package io

import java.io.{File, FileInputStream}

/**
  * Hasher implementation for file
  * 
  * @version 0.0.3
  * @note created on 2015-07-09
  * @author SourceCodeBot
  */
class FileHasher extends Hasher {

  /**
   * 
   * @param f [[File]]
   * @param algorithm
   * @return String
   */
  def getFileHash(f: File, algorithm: String = "md5"): String = getHash(new FileInputStream(f), algorithm)
}

object FileHasher {
  private[this] lazy val instance = new FileHasher

  def apply: FileHasher = instance

  def getFileHash(f: File, algorithm: String="md5"): String = instance.getFileHash(f, algorithm)
}