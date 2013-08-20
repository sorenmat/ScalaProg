package infrastructure.persistence

import com.mongodb.{DBCollection, BasicDBObject}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

/**
 * User: soren
 */
object SubscriptionRepository {
  val mapper = new ObjectMapper().registerModule(DefaultScalaModule)
  val subscriptions: DBCollection = MongoPersister.collection("subscriptions")

  def saveSubscription(email: String) = {
    val searchValue = new BasicDBObject("email", email)
    val searchResult = subscriptions.findOne(searchValue)
    if (searchResult != null)
      throw new RuntimeException("A subscription for email '" + email + "' is already saved in the repository")
    subscriptions.save(new BasicDBObject("email", email))

  }

  def subscriptionExists(email: String) = {
    val searchValue = new BasicDBObject("email", email)
    val searchResult = subscriptions.findOne(searchValue)
    if (searchResult == null)
      true
    else
      false
  }
}
