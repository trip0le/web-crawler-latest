package com.example.summarizer

import com.typesafe.config.ConfigFactory
import java.sql.{Connection, DriverManager, PreparedStatement}

object SummarizeLogger {
  private val config = ConfigFactory.load()
  private val dbUrl = config.getString("db.url")
  private val dbUser = config.getString("db.user")
  private val dbPassword = config.getString("db.password")

  def logSummaryRequest(url: String, summary: String): Unit = {
    val conn: Connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)

    val stmt: PreparedStatement = conn.prepareStatement(
      "INSERT INTO summarize_logs (url, summary, requested_at) VALUES (?, ?, now())"
    )

    stmt.setString(1, url)
    stmt.setString(2, summary)
    stmt.executeUpdate()

    stmt.close()
    conn.close()
  }
}
