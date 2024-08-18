import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Login from './login';
import Home from './home';
import Join from './join';
import AdminDashboard from './AdminDashboard'; // AdminDashboard 컴포넌트 추가

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/join" element={<Join />} />
        <Route path="/login" element={<Login />} />
        <Route path="/admin" element={<AdminDashboard />} /> {/* component 대신 element 사용 */}
        <Route path="/home" element={<Home />} />
        <Route path="/" element={<Login />} />
      </Routes>
    </Router>
  );
}

export default App;
