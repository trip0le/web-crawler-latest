package com.example.summarizer

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.compat.java8.FutureConverters._  // This import is necessary to convert CompletableFuture to scala.concurrent.Future

object Main extends App {

  // URL to summarize
  val urlToSummarize = "https://www.bbc.com"
  
  println(s"Starting summarization for URL: $urlToSummarize")

  // 1. Summarize and get the summary string (blocking until Future is completed)
  val summaryFuture: Future[String] = SummarizerBridge.summarize(urlToSummarize).toScala  // Convert CompletableFuture to Scala Future

  // Using Await.result to block and wait for the result with a 20 seconds timeout
  try {
    val summary: String = Await.result(summaryFuture, 20.seconds)  // Blocking until Future completes
    println(s"\n=== Summary for $urlToSummarize ===")
    println(summary)
  } catch {
    case e: Exception => println(s"Error while summarizing: ${e.getMessage}")
  }

  // 2. Add slight delay to ensure DB write happens before reading history
  println("\nWaiting before fetching history...")
  Thread.sleep(3000)  // Optional: You may remove this if not needed

  // 3. Fetch history and print it
  println("\n=== Fetching Summary History ===")
  val history = SummarizerBridge.getHistory

  // Check if history is not empty and print each entry
  if (history.isEmpty) {
    println("No history found.")
  } else {
    history.forEach(println)  // Print each history entry
  }
}
