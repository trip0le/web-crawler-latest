// src/pages/SummarizePage.jsx
import React, { useState } from 'react';

export default function SummarizePage() {
  const [url1, setUrl1] = useState('');
  const [url2, setUrl2] = useState('');
  const [summary, setSummary] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSummarize = async () => {
    if (!url1.trim() || !url2.trim()) return;
    setLoading(true);
    setSummary('');

    try {
      const res = await fetch(`/api/summarizer/summary?url1=${encodeURIComponent(url1)}&url2=${encodeURIComponent(url2)}`);
      const text = await res.text();
      setSummary(text);
    } catch (err) {
      console.error(err);
      setSummary("Error summarizing the URL.");
    }

    setLoading(false);
  };

  return (
    <div>
      <h1>Website Summarizer</h1>
      <input
        type="text"
        placeholder="Enter website1 URL"
        value={url1}
        onChange={(e) => setUrl1(e.target.value)}
      />

      <input
        type="text"
        placeholder="Enter website2 URL"
        value={url2}
        onChange={(e) => setUrl2(e.target.value)}
      />

      <button onClick={handleSummarize}>Summarize</button>
      {loading ? <p>Loading...</p> : <p>{summary}</p>}
    </div>
  );
}
