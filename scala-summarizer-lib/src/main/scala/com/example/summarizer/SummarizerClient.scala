package com.example.summarizer

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer

import scala.concurrent.{Future, ExecutionContext}

object SummarizerClient {
  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContext = system.dispatcher

  val summarizeUrl = "http://localhost:8000/summarize"

  def summarizeUrlRequest(url: String): Future[String] = {
    val jsonData = s"""{"url": "$url"}"""
    val entity = HttpEntity(ContentTypes.`application/json`, jsonData)

    val responseFuture: Future[HttpResponse] =
      Http().singleRequest(HttpRequest(method = HttpMethods.POST, uri = summarizeUrl, entity = entity))

    responseFuture.flatMap { response =>
      Unmarshal(response.entity).to[String]
    }
  }
}
