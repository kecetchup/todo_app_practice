import React from 'react';
import './item.css'; // CSS 파일 이름 수정

const Item = ({ todo, onDelete, onEdit }) => {
    return (
      <li>
        <span>{todo.content}</span>
        <div>
          <button onClick={() => onEdit(todo.id, todo.content)}>수정</button>
          <button onClick={onDelete}>삭제</button>
        </div>
      </li>
    );
  };
  
export default Item;
