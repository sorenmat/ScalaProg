package blogger

import java.text.SimpleDateFormat

/**
 * User: soren
 */
object DateHelper {

  def getYear(date: String) = date.substring(0, date.indexOf("-"))

  def getDay(date: String) = new SimpleDateFormat("MMM d").format(new SimpleDateFormat("yyyy-MM-dd").parse(date))
}
