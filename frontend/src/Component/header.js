import React from 'react';
import './header.css'
const Header = ({ data }) => {
  return (
    <header>
      <h1>Welcome to Home Page</h1>
      {data ? <p>{data}</p> : <p>Loading...</p>}
    </header>
  );
};

export default Header;
