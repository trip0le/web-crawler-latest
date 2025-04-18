package com.example.summarizer

import scala.util.{Failure, Success}
import scala.concurrent.{Future, ExecutionContext}
import scala.concurrent.ExecutionContext.Implicits.global

object SummarizeHandler {

  def handleSummarizeRequest(url: String): Future[String] = {
    SummarizerClient.summarizeUrlRequest(url).map { summary =>
      SummarizeLogger.logSummaryRequest(url, summary)
      summary
    }.recover {
      case ex =>
        println(s"Error occurred: ${ex.getMessage}")
        throw ex
    }
  }
}
