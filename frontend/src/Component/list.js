import React from 'react';
import Item from './Item';
import './list.css';

const List = ({ todos, onDelete }) => {
  return (
    <ul>
      {todos.map((todo) => (
        <Item key={todo.id} todo={todo} onDelete={() => onDelete(todo.id)} />
      ))}
    </ul>
  );
};

export default List;
