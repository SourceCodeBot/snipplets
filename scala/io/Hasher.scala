package io

import java.io.InputStream
import java.security.MessageDigest

/**
  * Hashing Object Trait
  * supports the generally algorithm like md5 and sha-1
  * 0.0.2 to 0.0.3
  * - update scala code style
  *
  * @see Hasher#getFileHash for File
  * @see Hasher#getStringHash for String
  * @author SourceCodeBot <dev@nils-heinemann.de> 11.08.2015.
  */
trait Hasher {

  protected val BUFFER_SIZE: Int = 1024

  /**
    * hashing the the content from inputstream to one selected hashing method.
    *
    * hash support [[supportedAlgorithmList]]
    *
    * @param stream    InputStream
    * @param algorithm Option one of the supported hashing algorithm
    * @return
    */
  def getHash(stream: InputStream, algorithm: Option[String]): String = {
    if (0 > supportedAlgorithmList.indexOf(algorithm.getOrElse("md5").toUpperCase))
      throw UnsupportedAlgorithmException
    val md = MessageDigest.getInstance(algorithm.getOrElse("md5").toUpperCase)
    val buffer = new Array[Byte](BUFFER_SIZE)
    Stream.continually(stream.read(buffer)).takeWhile(_ != -1).foreach(md.update(buffer, 0, _))
    md.digest.map(mapByte).mkString
  }

  protected def mapByte(byte: Byte): String = Integer.toString((byte & 0xff) + 0x100, 16).substring(1)

  /**
    * string array of all supported hashing algorithm supported:
    * <ul>
    * <li>MD2</li>
    * <li>MD5</li>
    * <li>SHA-1</li>
    * <li>SHA-224</li>
    * <li>SHA-256</li>
    * <li>SHA-384</li>
    * <li>SHA-512</li>
    * </ul>
    * @see https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#MessageDigest
    * @return
    */
  def supportedAlgorithmList = List("MD2", "MD5", "SHA-1", "SHA-224", "SHA-256", "SHA-384", "SHA-512")

  /**
    * customized exception for unsupported algorithm case
    */
  case object UnsupportedAlgorithmException extends Exception(s"try one of the supported algorithm: ${supportedAlgorithmList.mkString(", ")}")

}
