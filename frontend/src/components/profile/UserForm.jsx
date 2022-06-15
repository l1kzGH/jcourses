import React, {useEffect, useState} from 'react';
import {Button} from "react-bootstrap";
import axios from "axios";
import {NavLink} from "react-router-dom";

const UserForm = ({userInfo, setStatus}) => {

    const [follows, setFollows] = useState([]);
    const [subscribes, setSubscribes] = useState([]);

    useEffect(() => {
        if (userInfo.id != null) {
            axios({
                method: "get",
                url: "http://localhost:8080/follow/" + userInfo.id,
                withCredentials: true
            }).then((response) => {
                setFollows(response.data);
            });

            axios({
                method: "get",
                url: "http://localhost:8080/subscribe/" + userInfo.id,
                withCredentials: true
            }).then((response) => {
                setSubscribes(response.data);
            });

        }
    }, [userInfo])

    return (
        <div className="header header-primary">
            <Button className="editButton" variant="outline-info" onClick={() => setStatus(false)}>&#x270E;</Button>
            <div>
                <img width={150} height={150} className="imageClass"
                     src={"http://localhost:3000/profiles/" + userInfo.image_url}
                     alt="image"/>
            </div>

            <div className="description">
                <h1 className="h1c">{userInfo.name}</h1>
                <p>Никнейм: {userInfo.username}</p>
                <p>Роль: {userInfo.role}</p>
                <div className="line"></div>
            </div>

            <div className="description">
                <b>Подписки</b>
                {
                    follows.length == 0 ?
                        <p>*пусто*</p>
                        :
                        follows.map((follow) =>
                            <div key={follows.indexOf(follow)}>
                                <NavLink to={'/author/' + follow.author_username}>{follow.author_username + " "}</NavLink>
                            </div>
                        )
                }
                <div className="line"></div>
            </div>

            <div className="description">
                <b>Курсы</b>
                {
                    subscribes.length == 0 ?
                        <p>*пусто*</p>
                        :
                        subscribes.map((subscribe) =>
                            <div key={subscribes.indexOf(subscribe)}>
                                <NavLink to={'/courses/' + subscribe.course_id}>
                                    {subscribe.course_name + " "}
                                </NavLink>
                            </div>
                        )
                }
            </div>

            {
                userInfo.role == "AUTHOR" || userInfo.role == "ADMIN" ?
                    <div>
                        <div className="description">
                            <NavLink style={{color: "red"}} to={'/author/' + userInfo.username}>Страница автора</NavLink>
                        </div>
                    </div>
                    : null
            }

        </div>
    );
};

export default UserForm;