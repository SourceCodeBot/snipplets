package util

import java.io.File

/**
  * this builder use while append parts of value the system file separator as glue.
  *
  * processing methods return this instance self and some of the methods return the result.
  *
  * @param currentValue [[String]] inner value with current path value
  * @author SourceCodeBot <dev@nils-heinemann.de> 20.04.2018.
  */
class PathBuilder(private var currentValue: String = "", val separator: String = File.separator) {


  /**
    * append a part to the current value and receive the result
    * @param part [[String]] value to append to current value
    * @return [[String]] result value
    */
  def appendAndGet(part: String): String = append(part).currentValue

  /**
    * append a part to the current value and receive the builder self
    * @param part [[String]] value to append to current value
    * @return [[util.PathBuilder]] instance self
    */
  def append(part: String): PathBuilder = {
    this.currentValue = this.currentValue + this.separator + part
    this
  }

  /**
    * alias method
    *
    * @see [[append]] instead
    * @param part [[String]] value to append to current value
    * @return [[util.PathBuilder]] instance self
    */
  def +(part:String) = append(part)

  /**
    * alias method
    *
    * @see [[appendAndGet]] instead
    * @param part [[String]] value to append to current value
    * @return [[String]] result value
    */
  def +=(part:String) = appendAndGet(part)

  /**
    * returning the currentValue
    * @return [[String]] current value
    */
  def get(): String = currentValue

  /**
    * maybe you want to append a part only if a condition is matching.
    *
    * @param part [[String]] value to append to current value
    * @param condition [[scala.Boolean]] condition which control appending or not
    * @return [[util.PathBuilder]] instance self
    */
  def conditionalAppend(part: String, condition: Boolean): PathBuilder = {
    if (condition) {
      append(part)
    }
    this
  }

  /**
    * maybe you want to append a part only if a condition is matching.
    *
    * @param part [[String]] value to append to current value
    * @param condition [[Function0]] condition which control appending or not
    * @return [[util.PathBuilder]] instance self
    */
  def conditionalAppend(part: String, condition: () => Boolean): PathBuilder = conditionalAppend(part, condition.apply())

  /**
    * maybe you want to append a part only if your condition depend on the current value is matching.
    *
    * in your method the builder set the current value to apply.
    *
    * @param part [[String]] value to append to current value
    * @param condition [[Function1]] condition which control appending or not. 
    * @return [[util.PathBuilder]] instance self
    */
  def conditionalAppendOnValue(part: String, condition: String => Boolean): PathBuilder = conditionalAppend(part, condition(currentValue))
}

/**
  * facade for [[util.PathBuilder]]
  */
object PathBuilder {

  /**
    * simple apply factory method
    * @return [[util.PathBuilder]]
    */
  def apply(): PathBuilder = new PathBuilder()

  /**
    * apply instance which initial value
    * @param init initial value to set to builder
    * @return [[util.PathBuilder]]
    */
  def apply(init: String): PathBuilder = new PathBuilder(init)

  /**
    * apply instance and append all parts to it.
    *
    * @param parts [[scala.Array]] of [[String]]s to append.
    * @return [[util.PathBuilder]]
    */
  def apply(parts: Array[String]): PathBuilder = {
    val r = apply()
    parts.foreach(r.append)
    r
  }
}