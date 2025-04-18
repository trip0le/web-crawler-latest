// src/pages/HistoryPage.jsx
import React, { useEffect, useState } from 'react';

export default function HistoryPage() {
  const [history, setHistory] = useState([]);

  const fetchHistory = async () => {
    try {
      const res = await fetch('/api/summarizer/history');
      const data = await res.json();
      setHistory(data);
    } catch (err) {
      console.error('Error fetching history:', err);
    }
  };

  useEffect(() => {
    fetchHistory();
  }, []);

  const deleteHistory = async (id) => {
    try {
      await fetch(`/api/summarizer/deleteHistory?id=${id}`, {
        method: 'DELETE',
      });
      await fetchHistory();
    } catch (err) {
      console.error('Error deleting history:', err);
    }
  };

  return (
    <div>
      <h1>Summary History</h1>
      <ul>
        {history.map((entry) => (
          <li key={entry.id} style={{ marginBottom: '1rem' }}>
            <div><strong>ID:</strong> {entry.id}</div>
            <div><strong>URL:</strong> {entry.url}</div>
            <div><strong>Requested At:</strong> {entry.requestedAt}</div>
            <div><strong>Summary:</strong></div>
            <pre style={{ backgroundColor: '#f0f0f0', padding: '0.5rem' }}>{entry.summary}</pre>
            <button onClick={() => deleteHistory(entry.id)}>Delete History</button>
          </li>
        ))}
      </ul>
    </div>
  );
}
