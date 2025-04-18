package com.example.summarizer

import com.typesafe.config.ConfigFactory
import java.sql.{Connection, DriverManager, PreparedStatement}

object DeleteHistory {
  private val config = ConfigFactory.load()
  private val dbUrl = config.getString("db.url")
  private val dbUser = config.getString("db.user")
  private val dbPassword = config.getString("db.password")

  def deleteSummaryRequest(historyId: Int): Unit = {
    val conn: Connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)

    val stmt: PreparedStatement = conn.prepareStatement(
      "DELETE FROM summarize_logs WHERE id = ?"
    )

    stmt.setInt(1, historyId)
    stmt.executeUpdate()

    stmt.close()
    conn.close()
  }
}