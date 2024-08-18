import React, { useEffect, useState } from 'react';
import './home.css'; // CSS 파일 import
import Header from './Component/header';
import List from './Component/list';

const Home = () => {
  const [data, setData] = useState(null);
  const [error, setError] = useState(null);
  const [todos, setTodos] = useState([]);
  const [newTodo, setNewTodo] = useState('');
  const [loading, setLoading] = useState(true); // 로딩 상태 추가

  const fetchProtectedData = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/home', {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });

      if (!response.ok) {
        throw new Error('Network response was not ok');
      }

      const jsonData = await response.json();
      setData(jsonData.message);
    } catch (error) {
      console.error('There was a problem with your fetch operation:', error);
      setError(error.message);
    }
  };

  const fetchTodos = async () => {
    setLoading(true); // 로딩 시작
    try {
      const response = await fetch('http://localhost:8080/api/todos', {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });

      if (!response.ok) {
        throw new Error('Network response was not ok');
      }

      const todosData = await response.json();
      setTodos(todosData);
    } catch (error) {
      console.error('There was a problem with your fetch operation:', error);
      setError(error.message);
    } finally {
      setLoading(false); // 로딩 끝
    }
  };

  const addTodo = async () => {
    if (newTodo.trim() !== '') {
      try {
        const response = await fetch('http://localhost:8080/api/todos', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
          },
          body: JSON.stringify({ todo: newTodo }),
        });

        if (!response.ok) {
          throw new Error('Network response was not ok');
        }

        setNewTodo('');
        await fetchTodos(); // 새로고침
      } catch (error) {
        console.error('There was a problem with your fetch operation:', error);
        setError(error.message);
      }
    }
  };

  const deleteTodo = async (id) => {
    try {
      const response = await fetch(`http://localhost:8080/api/todos/${id}`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });

      if (!response.ok) {
        throw new Error('Network response was not ok');
      }

      await fetchTodos(); // 새로고침
    } catch (error) {
      console.error('There was a problem with your fetch operation:', error);
      setError(error.message);
    }
  };

  useEffect(() => {
    fetchProtectedData();
    fetchTodos();
  }, []);

  return (
    <div>
      <Header data={data} />
      {error && <p>Error: {error}</p>}
      {loading ? <p>Loading...</p> : (
        <>
          <h2>Todo List</h2>
          <input
            type="text"
            value={newTodo}
            onChange={(e) => setNewTodo(e.target.value)}
            placeholder="Add a new todo"
          />
          <button onClick={addTodo}>Add Todo</button>
          <List todos={todos} onDelete={deleteTodo} />
        </>
      )}
    </div>
  );
};

export default Home;
