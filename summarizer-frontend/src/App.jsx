// src/App.jsx
import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import SummarizePage from './pages/SummarizePage';
import HistoryPage from './pages/HistoryPage';

function App() {
  return (
    <Router>
      <nav>
        <Link to="/">Summarize</Link> | <Link to="/history">History</Link>
      </nav>

      <Routes>
        <Route path="/" element={<SummarizePage />} />
        <Route path="/history" element={<HistoryPage />} />
      </Routes>
    </Router>
  );
}

export default App;
