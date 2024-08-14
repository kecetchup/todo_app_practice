import React from 'react';
import './login.css'; // CSS 파일을 import

const Login = () => {
  return (
    <div className="login-container">
      <div className="login-form">
        <h1>Login</h1>
        <input type="text" placeholder="Username" />
        <input type="password" placeholder="Password" />
        <button>Login</button>
      </div>
    </div>
  );
}

export default Login;
