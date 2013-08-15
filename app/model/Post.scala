package model

import java.util.UUID

case class Post(id: UUID, title: String, summary: String, val date: String, tags: List[String], author: String, content: String, var comments: List[Comment])

case class Comment(author: String, authorEmail: String, comment: String)