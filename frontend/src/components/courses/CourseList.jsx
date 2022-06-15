import React from 'react';
import CourseItem from "./CourseItem";

const CourseList = ({title, courses}) => {

    if (!courses.length) {
        return (
            <h1 style={{textAlign: 'center', color: "white"}}>
                {title} не найдены!
            </h1>
        )
    }

    return (
        <div>
            <h1 style={{textAlign: 'center', color: "white"}}>
                {title}
            </h1>
                {courses.map((course) =>
                        <CourseItem course={course} key={courses.indexOf(course)}/>
                )}
        </div>
    );
};

export default CourseList;