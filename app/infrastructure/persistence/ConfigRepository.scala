package infrastructure.persistence

import com.mongodb.{MongoClientURI, BasicDBObject, MongoClient}
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

  val connectionURL = if (System.getenv("MONGOLAB_URI") != null) System.getenv("MONGOLAB_URI") else "mongodb://127.0.0.1:27017/scalablog"
  private val mongoURI: MongoClientURI = new MongoClientURI(connectionURL)
  val client = new MongoClient(mongoURI)

  val db = client.getDB(mongoURI.getDatabase)

  if (mongoURI.getUsername() != null) {
    println("Authenticate MongoLab")
    db.authenticate(mongoURI.getUsername(), mongoURI.getPassword())
  }
  val postColl = db.getCollection("posts")



  def getCreatePassword = {
    val searchValue = new BasicDBObject()
    val searchResult = postColl.findOne(searchValue)
    if(searchResult == null)
      throw new RuntimeException("App not configured")
    searchResult.get("createPassword").toString
  }
}
