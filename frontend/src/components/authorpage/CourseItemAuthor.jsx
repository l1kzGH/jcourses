import React from 'react';
import {useHistory} from "react-router-dom";

const CourseItemAuthor = ({course}) => {
    const router = useHistory();

    return (
        <button className="courseAuthor" onClick={() => router.push(`/courses/${course.id}`)}>
            <img width={100} height={100} src={"http://localhost:3000/courses/" + course.image_url}></img>
            <div >
                <strong>{course.name}</strong>
            </div>
        </button>
    );
};

export default CourseItemAuthor;