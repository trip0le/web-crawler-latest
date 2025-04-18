package com.example.summarizer

import java.util.concurrent.CompletableFuture
import scala.concurrent.ExecutionContext.Implicits.global
import scala.compat.java8.FutureConverters._

object SummarizerBridge {

  def summarize(url: String): CompletableFuture[String] = {
    val scalaFuture = SummarizeHandler.handleSummarizeRequest(url)
    scalaFuture.toJava.toCompletableFuture
  }

  def getHistory: java.util.List[String] = {
    import scala.jdk.CollectionConverters._
    SummarizeHistoryReader.fetchHistory().asJava
  }
}
