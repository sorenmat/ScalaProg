package infrastructure.persistence

import com.mongodb.BasicDBObject
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

/**
 * User: soren
 */
object ConfigRepository {
  val mapper = new ObjectMapper().registerModule(DefaultScalaModule)

  def getCreatePassword = {
    val searchValue = new BasicDBObject()
    val searchResult = MongoPersister.collection("config").findOne(searchValue)
    if (searchResult == null)
      throw new RuntimeException("App not configured")
    searchResult.get("createPassword").toString
  }
}
