package controllers

import play.api.mvc._
import model.Comment
import play.api.data._
import play.api.data.Forms._
import infrastructure.persistence.{SubscriptionRepository, ConfigRepository, PostRepository}
import model.Post
import java.text.SimpleDateFormat
import java.util.{UUID, Date}
import model.Comment
import model.Post
import model.Comment
import model.Post

object Application extends Controller {


  def index = Action {

    Ok(views.html.index(PostRepository.allPosts, subscriptionForm, None))
  }

  def getPost(id: UUID) = Action {
    Ok(views.html.post(PostRepository.getPostFromID(id), commentForm))
  }


  val postForm = Form(
    tuple(
      "title" -> nonEmptyText,
      "summary" -> nonEmptyText,
      "content" -> nonEmptyText,
      "tags" -> nonEmptyText,
      "password" -> nonEmptyText.verifying("doesn't match", con => con == ConfigRepository.getCreatePassword)
    )
  )

  def createPost = Action {
    Ok(views.html.createPost(postForm))
  }

  def submitPost = Action {
    implicit request =>
      postForm.bindFromRequest.fold(
        formWithErrors => {
          println("Failure....")
          BadRequest(views.html.createPost(formWithErrors))

        },
        value => {
          println("Success....")
          val title: String = value._1
          val summary: String = value._2
          val tags: String = value._4
          val content: String = value._3
          val date = new SimpleDateFormat("yyyy-MM-dd").format(new Date())
          val postId: UUID = UUID.randomUUID()
          val post = Post(postId, title, summary, date, tags.split(" ").toList, "Soren Mathiasen", content, Nil)
          println("Sa")
          PostRepository.savePost(post)
          Redirect(routes.Application.getPost(postId))
          //Ok(views.html.post(post))
        }
      )

  }

  val commentForm = Form(
    tuple(
      "author" -> nonEmptyText,
      "authorEmail" -> text,
      "comment" -> nonEmptyText
    )
  )

  def submitComment(id: UUID) = Action {
    implicit request =>
      commentForm.bindFromRequest.fold(
        commentFormWithErrors => {
          println("Failure....")
          BadRequest(views.html.post(null, commentFormWithErrors))

        },
        value => {
          println("Success....")
          val author: String = value._1
          val authorEmail: String = value._2
          val commentTxt: String = value._3
          val date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())
          val comment = Comment(author, authorEmail, commentTxt)
          println(comment)

          val post = PostRepository.getPostFromID(id)
          post.comments = comment :: post.comments

          PostRepository.updatePost(post)
          Redirect(routes.Application.getPost(id))
        }
      )

  }


  val subscriptionForm = Form(
    single(
      "subscribe" -> email.verifying("Email not unique", email => SubscriptionRepository.subscriptionExists(email))
    )    )

  def submitSubscription = Action {
    implicit request =>
      subscriptionForm.bindFromRequest.fold(
        formWithErrors => {
          println("Failure....")
          BadRequest(views.html.index(PostRepository.allPosts, formWithErrors, Some(false)))

        },
        enteredEmail => {
          SubscriptionRepository.saveSubscription(enteredEmail)
          println("Successful subscription "+enteredEmail)
          Ok(views.html.index(PostRepository.allPosts, subscriptionForm, Some(true)))
        }
      )

  }


}