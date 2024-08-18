import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './login.css';

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch('http://localhost:8080/api/login', {
        method: 'POST',
        mode: 'cors',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
      });

      if (!response.ok) {
        throw new Error('Network response was not ok');
      }

      const data = await response.text();

      if (response.status === 200) {
        // 로그인 성공 시 JWT를 로컬 스토리지에 저장
        localStorage.setItem('token', data);
        navigate('/home'); // 홈 페이지로 이동
      } else {
        alert('Invalid username or password');
      }
    } catch (error) {
      console.error('There was a problem with your fetch operation:', error);
    }
  };

  const handleCreateAccount = () => {
    navigate('/join'); // 회원가입 페이지로 이동
  };

  return (
    <div className="login-container">
      <div className="login-form">
        <h1>Login</h1>
        <form onSubmit={handleSubmit}>
          <input
            type="text"
            placeholder="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <button type="submit">Login</button>
          <button type="button" onClick={handleCreateAccount}>Create Account</button>
        </form>
      </div>
    </div>
  );
};

export default Login;
