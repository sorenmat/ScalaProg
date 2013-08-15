package infrastructure.persistence

import com.mongodb.{BasicDBObject, MongoClient}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import java.io.StringWriter
import model.Post

/**
 * User: soren
 */
object ConfigRepository {
  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)

  val mongoClient = new MongoClient()
  val db = mongoClient.getDB("scalablog");
  val postColl = db.getCollection("config");

  def getCreatePassword = {
    val searchValue = new BasicDBObject()
    val searchResult = postColl.findOne(searchValue)
    if(searchResult == null)
      throw new RuntimeException("App not configured")
    searchResult.get("createPassword").toString
  }
}
