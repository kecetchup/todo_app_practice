import React from 'react';
import Item from './Item';
import './list.css';

const List = ({ todos, onDelete, onEdit }) => {
    return (
        <ul>
            {todos.map(todo => (
                <Item 
                    key={todo.id} 
                    todo={todo} 
                    onDelete={() => onDelete(todo.id)} 
                    onEdit={onEdit} 
                />
            ))}
        </ul>
    );
};

export default List;
