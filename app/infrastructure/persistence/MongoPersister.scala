package infrastructure.persistence

import com.mongodb.{MongoClient, MongoClientURI}

/**
 * User: soren
 */
object MongoPersister {

  val connectionURL = if (System.getenv("MONGOLAB_URI") != null) System.getenv("MONGOLAB_URI") else "mongodb://127.0.0.1:27017/scalablog"
  private val mongoURI: MongoClientURI = new MongoClientURI(connectionURL)
  val client = new MongoClient(mongoURI)

  val db = client.getDB(mongoURI.getDatabase)

  if (mongoURI.getUsername() != null) {
    println("Authenticate MongoLab")
    db.authenticate(mongoURI.getUsername(), mongoURI.getPassword())
  }

  def collection(name: String) = db.getCollection(name)
}
