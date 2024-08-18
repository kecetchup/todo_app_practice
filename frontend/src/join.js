import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './login.css';

const Join = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [email, setEmail] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (password !== confirmPassword) {
      alert('Passwords do not match');
      return;
    }

    try {
      const response = await fetch('http://localhost:8080/api/register', {
        method: 'POST',
        mode: 'cors',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password, email }), // 이메일 추가
      });

      if (!response.ok) {
        throw new Error('Network response was not ok');
      }

      const data = await response.text();
      console.log(data);

      if (response.status === 201) {
        alert('Account created successfully');
        navigate('/login');
      } else {
        alert('Error creating account');
      }
    } catch (error) {
      console.error('There was a problem with your fetch operation:', error);
    }
  };

  return (
    <div className="login-container">
      <div className="login-form">
        <h1>Join</h1>
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
          <input
            type="email"
            placeholder="example@email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <input
            type="password"
            placeholder="Confirm Password"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
          />
          
          <button type="submit">Create Account</button>
        </form>
      </div>
    </div>
  );
};

export default Join;
