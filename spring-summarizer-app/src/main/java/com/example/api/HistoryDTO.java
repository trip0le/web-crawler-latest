package com.example.api;

import java.sql.Timestamp;

public class HistoryDTO {
    public int id;
    public String url;
    public String summary;
    public Timestamp requestedAt;

    public HistoryDTO(int id, String url, String summary, Timestamp requestedAt) {
        this.id = id;
        this.url = url;
        this.summary = summary;
        this.requestedAt = requestedAt;
    }
}
