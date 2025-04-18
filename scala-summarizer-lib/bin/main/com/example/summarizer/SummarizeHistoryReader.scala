package com.example.summarizer

import com.typesafe.config.ConfigFactory
import java.sql.{Connection, DriverManager, ResultSet}
import scala.collection.mutable.ListBuffer

object SummarizeHistoryReader {
  private val config = ConfigFactory.load()
  private val dbUrl = config.getString("db.url")
  private val dbUser = config.getString("db.user")
  private val dbPassword = config.getString("db.password")

  def fetchHistory(): List[String] = {
    val conn: Connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)
    val stmt = conn.createStatement()
    val rs: ResultSet = stmt.executeQuery("SELECT id, url, summary, requested_at FROM summarize_logs ORDER BY requested_at DESC")

    val historyBuffer = ListBuffer[String]()

    while (rs.next()) {
      val id = rs.getInt("id")
      val url = rs.getString("url")
      val summary = rs.getString("summary")
      val timestamp = rs.getTimestamp("requested_at")
      val entry = s"[$timestamp] Summary #$id\nURL: $url\nSummary: $summary"
      historyBuffer += entry
    }

    rs.close()
    stmt.close()
    conn.close()

    historyBuffer.toList
  }
}
