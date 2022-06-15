import React from 'react';
import "../../styles/course.css";
import {useHistory} from "react-router-dom";

const CourseItem = ({course}) => {
    const router = useHistory();

    return (
        <button className="block" onClick={() => router.push(`/courses/${course.id}`)}>
            <img width={150} height={150} className="image" src={"http://localhost:3000/courses/" + course.image_url}></img>
            <div className="text">
                <strong className="name">{course.name}</strong>
                <div className="categories">{course.category_name.map((name) => '[' + name + ']')}</div>
                <div className="author">Автор: {course.author}</div>
                <div className="rating">Рейтинг: {Math.round(course.rating * 100)/100}&#x2730; ({course.number_votes})</div>
                <div className="price">Стоимость: {course.price} руб.</div>
            </div>
        </button>
    );
};

export default CourseItem;