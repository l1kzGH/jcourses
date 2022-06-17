import React from 'react';
import "../navbar.css";
import {NavLink} from "react-router-dom";
import axios from "axios";

const NotifyItem = ({notify, index, getNotifies}) => {

    let topH = index * 100 + 38;

    const removeNotify = () => {
        axios({
            method: "delete",
            url: "http://localhost:8080/notification/" + notify.id,
            withCredentials: true
        }).then((response) => {
            console.log("Удален " + notify.id);
            getNotifies();
        });
    }

    return (
        <div style={{top: topH, display: "none"}} className="notifyMain">
            <div className="notifyTextHead">
                Уведомление о новом курсе
            </div>

            <div className="authorLine"></div>

            <div className="notifyTextBody">
                Автор <NavLink style={{textDecoration: "none"}}
                               to={`/author/${notify.author_username}`}>{notify.author_username}</NavLink> открыл доступ
                к курсу <NavLink style={{textDecoration: "none"}}
                                 to={`/courses/${notify.course_id}`}>{notify.course_name}</NavLink>.
            </div>

            <span className="close" onClick={removeNotify}></span>
        </div>
    );
};

export default NotifyItem;