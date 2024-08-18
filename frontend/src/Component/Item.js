import React from 'react';
import './item.css';

const Item = ({ todo, onDelete }) => {
  return (
    <li>
      <span>{todo.content}</span> {/* todo의 content 속성을 렌더링 */}
      <button onClick={onDelete}>삭제</button>
    </li>
  );
};

export default Item;
