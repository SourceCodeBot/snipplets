package io

import java.io.InputStream
import java.security.MessageDigest

/**
  * Hashing Object Trait
  * supports the generally algorithm like md5 and sha-1
  * full list of supported algorithm see in getListOfSupportedAlgorithm
  * @see Hasher#getFileHash for File
  * @see Hasher#getStringHash for String
  *
  * @version 0.0.3
  * @note created on 2015-05-05
  * @author SourceCodeBot
  */
trait Hasher {

  /**
    *
    * hash support @see Hasher#getListOfSupportedAlgorithm
    * <b>Hint</b>: returns lowercase string!
    *
    * @param stream [InputStream]
    * @param algorithm one of the supported hashing algorithm
    * @return String
    */
  def getHash(stream: InputStream, algorithm: String = "md5"): String = {
    if(0>getListOfSupportedAlgorithm.indexOf(algorithm)) throw UnsupportedAlgorithmException
    val md = MessageDigest.getInstance(algorithm)
    val sb = new StringBuffer
    val buffer = new Array[Byte](1024)
    Stream.continually(stream.read(buffer)).takeWhile(_ != -1).foreach(md.update(buffer,0,_))
    md.digest.foreach(b => sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1)))
    sb.toString
  }

  /**
    * string array of all supported hashing algorithm supported:
    * <ul>
    * <li>SHA-512</li>
    * <li>SHA1</li>
    * <li>MD2</li>
    * <li>SHA</li>
    * <li>SHA ImplementedIn</li>
    * <li>SHA-256</li>
    * <li>MD5 ImplementedIn</li>
    * <li>SHA-1</li>
    * <li>MD5</li>
    * <li>SHA-384</li>
    * </ul>
    * @return
    */
  def getListOfSupportedAlgorithm: Array[String] = Array[String]("MD2", "MD5", "SHA", "SHA1", "SHA-1", "SHA-256", "SHA-384", "SHA-512")

  case object UnsupportedAlgorithmException extends Exception{
    override def getMessage: String = "try one of the supported algorithm: "+getListOfSupportedAlgorithm.mkString(", ")
  }
}
