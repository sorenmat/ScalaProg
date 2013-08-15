package infrastructure.persistence

import com.mongodb.{MongoClientURI, MongoURI, BasicDBObject, MongoClient}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import java.io.StringWriter
import model.Post
import java.util.UUID

/**
 * User: soren
 */
object PostRepository {
  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)

  val connectionURL = if (System.getenv("MONGOLAB_URI") != null) System.getenv("MONGOLAB_URI") else "mongodb://127.0.0.1:27017/hello"
  private val mongoURI: MongoClientURI = new MongoClientURI(connectionURL)
  val client = new MongoClient(mongoURI)

  val db = client.getDB("scalablog")

  if (mongoURI.getUsername() != null) {
    println("Authenticate MongoLab")
    db.authenticate(mongoURI.getUsername(), mongoURI.getPassword());
  }
  val postColl = db.getCollection("posts");

  def savePost(post: Post) {
    require(post.id != null)
    val saveObject = new BasicDBObject()
    saveObject.put("post", getJson(post))
    saveObject.put("id", post.id.toString)

    postColl.save(saveObject)
  }

  def updatePost(post: Post) {
    require(post.id != null)
    val saveObject = new BasicDBObject()
    saveObject.put("post", getJson(post))
    saveObject.put("id", post.id.toString)

    postColl.update(new BasicDBObject("id", post.id.toString), saveObject)
  }

  def getJson(post: Post) = {
    val writer = new StringWriter()
    mapper.writeValue(writer, post)
    writer.close()
    writer.toString
  }

  def allPosts = {
    var posts: List[Post] = Nil
    val cursor = postColl.find()
    while (cursor.hasNext) {
      val obj = cursor.next().get("post").asInstanceOf[String]
      val post = mapper.readValue(obj, classOf[Post])
      posts = post :: posts
    }
    cursor.close()
    posts
  }

  def getPostFromID(id: UUID) = {
    val searchValue = new BasicDBObject("id", id.toString)
    val searchResult = postColl.findOne(searchValue)
    val obj = searchResult.get("post").asInstanceOf[String]
    mapper.readValue(obj, classOf[Post])
  }
}
