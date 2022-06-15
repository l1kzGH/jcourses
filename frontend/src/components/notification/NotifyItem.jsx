import React from 'react';
import "../navbar.css";
import {NavLink} from "react-router-dom";

const NotifyItem = ({notify, index}) => {

    let topH = index * 138;
    if(topH === 0) topH = 38;

    return (
        <div style={{top: topH, display: "none"}} className="notifyMain">
            <div className="notifyTextHead">
                Уведомление о новом курсе
            </div>

            <div className="authorLine"></div>

            <div className="notifyTextBody">
                Автор <NavLink to={`/author/${notify.author_username}`}>{notify.author_username}</NavLink> открыл доступ
                к курсу <NavLink to={`/courses/${notify.course_id}`}>{notify.course_name}</NavLink>.
            </div>
        </div>
    );
};

export default NotifyItem;