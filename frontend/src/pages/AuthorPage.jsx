import React, {useContext, useEffect, useState} from 'react';
import axios from "axios";
import {useHistory} from "react-router-dom";
import CourseItemAuthor from "../components/authorpage/CourseItemAuthor";
import "../styles/authorpage.css";
import {AuthContext} from "../context/authContext";
import {Button} from "react-bootstrap";

const AuthorPage = () => {

    const history = useHistory();
    const {username, id} = useContext(AuthContext);

    const [authorInfo, setAuthorInfo] = useState({});
    const [courses, setCourses] = useState([]);
    const [invisibleCourses, setInvisibleCourses] = useState([]);

    const [isFollow, setIsFollow] = useState(false);

    useEffect(() => {
        if (authorInfo.username != null)
            axios({
                method: "get",
                url: "http://localhost:8080/courses",
                params:
                    {
                        "count": 100,
                        "page": 1,
                        "author": authorInfo.username,
                        "visibility": "true"
                    },
                headers: {"Content-Type": "multipart/form-data"},
                withCredentials: true
            }).then((response) => {
                setCourses(response.data);
            });

        if (authorInfo.username === username && authorInfo.username != null)
            axios({
                method: "get",
                url: "http://localhost:8080/courses",
                params:
                    {
                        "count": 100,
                        "page": 1,
                        "author": authorInfo.username,
                        "visibility": "false"
                    },
                headers: {"Content-Type": "multipart/form-data"},
                withCredentials: true
            }).then((response) => {
                setInvisibleCourses(response.data);
            });
    }, [authorInfo])

    useEffect(() => {
        axios({
            method: "get",
            url: "http://localhost:8080/user" + window.location.pathname,
            withCredentials: true
        }).then((response => {
            if (response.data.role === "USER")
                history.push('/error');

            setAuthorInfo({
                id: response.data.id,
                name: response.data.name,
                username: response.data.username,
                image_url: response.data.image_url
            });
        })).catch((error) => {
            history.push('/error');
        });
    }, []);

    const follow = () => {
        if (username != null && authorInfo.username != null)
            axios({
                method: "post",
                url: "http://localhost:8080/follow",
                data:
                    {
                        user_username: username,
                        author_username: authorInfo.username
                    },
                withCredentials: true
            }).then((response) => {
                setIsFollow(true);
            });
    }

    const unfollow = () => {
        axios({
            method: "delete",
            url: "http://localhost:8080/follow",
            data: {
                user_username: username,
                author_username: authorInfo.username
            },
            withCredentials: true
        }).then((response) => {
            setIsFollow(false);
        });
    }

    useEffect(() => {
        if (username != null && authorInfo.username != null && username != authorInfo.username) {
            axios({
                method: "get",
                url: "http://localhost:8080/follow",
                params:
                    {
                        user_username: username,
                        author_username: authorInfo.username
                    },
                headers: {"Content-Type": "multipart/form-data"},
                withCredentials: true
            }).then((response) => {
                setIsFollow(response.data);
            });
        }
    }, [isFollow, authorInfo]);

    return (
        <div>
            <div className="container">
                <div className="row">
                    <div className="col-md-8 col-md-offset-3 col-sm-offset-3">
                        <div className="card">
                            <h2>Страница автора</h2>
                            <div className="header header-primary" style={{marginTop: -20}}>

                                <div>
                                    <img width={120} height={120} className="imageClass"
                                         src={"http://localhost:3000/profiles/" + authorInfo.image_url}
                                         alt="image"/>
                                </div>

                                <div className="description">
                                    <p style={{fontWeight: "normal", fontSize: 18, marginBottom: 5}}>
                                        Никнейм: {authorInfo.username}
                                    </p>
                                    {
                                        username != authorInfo.username && username != null ?
                                            !isFollow ?
                                                <Button size="sm" variant="outline-success"
                                                        onClick={follow}>Подписаться</Button>
                                                :
                                                <Button size="sm" variant="outline-danger"
                                                        onClick={unfollow}>Отписаться</Button>
                                            :
                                            null
                                    }

                                </div>

                                <div className="authorLine"></div>
                                <b>Курсы автора</b>
                                <div className="coursesListAuthor">
                                    {
                                        courses.map((course) =>
                                            <CourseItemAuthor key={courses.indexOf(course)} course={course}/>
                                        )
                                    }
                                </div>

                                {
                                    username === authorInfo.username && authorInfo.username != null ?
                                        <div>
                                            <p className="authorLine"></p>
                                            <b>Невышедшие курсы <a style={{color: "red"}}>[админ-панель]</a></b>
                                            <div className="coursesListAuthor">
                                                {
                                                    invisibleCourses.map((course) =>
                                                        <CourseItemAuthor course={course}/>
                                                    )
                                                }
                                            </div>
                                        </div>
                                        : null
                                }

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AuthorPage;