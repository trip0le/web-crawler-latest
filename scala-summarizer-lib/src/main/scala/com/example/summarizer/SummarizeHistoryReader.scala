package com.example.summarizer

import com.typesafe.config.ConfigFactory
import java.sql.{Connection, DriverManager, ResultSet, Timestamp}
import scala.collection.mutable.ListBuffer

object SummarizeHistoryReader {
  private val config = ConfigFactory.load()
  private val dbUrl = config.getString("db.url")
  private val dbUser = config.getString("db.user")
  private val dbPassword = config.getString("db.password")

  // Anonymous inner case class (nested in object)
  case class History(
    id: Int,
    url: String,
    summary: String,
    requestedAt: Timestamp
  )

  def fetchHistory(): List[History] = {
    val conn: Connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)
    val stmt = conn.createStatement()
    val rs: ResultSet = stmt.executeQuery(
      "SELECT id, url, summary, requested_at FROM summarize_logs ORDER BY requested_at DESC"
    )

    val historyBuffer = ListBuffer[History]()

    while (rs.next()) {
      val id = rs.getInt("id")
      val url = rs.getString("url")
      val summary = rs.getString("summary")
      val requestedAt = rs.getTimestamp("requested_at")

      historyBuffer += History(id, url, summary, requestedAt)
    }

    rs.close()
    stmt.close()
    conn.close()

    historyBuffer.toList
  }
}
