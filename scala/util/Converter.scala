package util

import java.time.{LocalDateTime, ZoneId}
import java.util.concurrent.TimeUnit

/**
  * @author SourceCodeBot <dev@nils-heinemann.de> 11.08.2015.
  */
object Converter {

  /**
    * convert milliseconds ([[scala.Long]]) to string with unit information.
    *
    * Example:
    * {{{
    *   longToTimeString(120000) => "2 minutes"
    *   longToTimeString(123000) => "2 minutes 30 seconds"
    *   longToTimeString(123001) => "2 minutes 30 seconds 1 millisecond"
    * }}}
    *
    * supports:
    * <ul>
    *   <li>day</li>
    *   <li>hour</li>
    *   <li>minute</li>
    *   <li>second</li>
    *   <li>millisecond</li>
    * </ul>
    *
    * @param long [[scala.Long]] to convert to [[String]]
    * @return [[String]]
    */
  def longToTimeString(long: Long): String = {
    val map = prepareTimeToString(long)
    map filter(_._2 > 0) map {
      case (key, value) if value > 1 => s"$value ${key}s"
      case (key, value) => s"$value $key"
    } mkString " "
  }

  /**
    * prepares a milliseconds value to a [[scala.collection.immutable.Map]] with Unit -> Value.
    *
    * @param millis [[scala.Long]] milliseconds
    * @return [[scala.collection.immutable.Map]] prepared map with all units and them values from millis
    */
  private def prepareTimeToString(millis: Long): Map[String, Long] = {
    val days = TimeUnit.MILLISECONDS.toDays(millis)
    val hours = TimeUnit.MILLISECONDS.toHours(millis - TimeUnit.DAYS.toMillis(days))
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis - TimeUnit.DAYS.toMillis(days) - TimeUnit.HOURS.toMillis(hours))
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millis - TimeUnit.DAYS.toMillis(days) - TimeUnit.HOURS.toMillis(hours) - TimeUnit.MINUTES.toMillis(minutes))
    val milliseconds = millis - TimeUnit.DAYS.toMillis(days) - TimeUnit.HOURS.toMillis(hours) - TimeUnit.MINUTES.toMillis(minutes) - TimeUnit.SECONDS.toMillis(seconds)
    Map("day" -> days, "hour" -> hours, "minute" -> minutes, "second" -> seconds, "millisecond" -> milliseconds)
  }


  /**
    * convert [[java.time.LocalDateTime]] to [[scala.Long]]
    *
    * @param localDataTime [[java.time.LocalDateTime]]  instance to convert
    * @param zoneId        [[Option]] optional [[java.time.ZoneId]] to take, default is systemdefault
    * @return millis
    */
  def toTimestamp(localDataTime: LocalDateTime, zoneId: Option[ZoneId] = None): Long =
    localDataTime.atZone(zoneId.getOrElse(ZoneId.systemDefault()))
      .toInstant
      .toEpochMilli

}
