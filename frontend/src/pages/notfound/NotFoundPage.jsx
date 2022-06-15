import React from 'react';
import "./notfoundpage.css";
import {NavLink} from "react-router-dom";

const NotFoundPage = () => {
    return (
        <div>
            <div className="notFoundBody">
            <section className="notFound">
                <div className="img">
                    <img src="https://assets.codepen.io/5647096/backToTheHomepage.png" alt="Back to the Homepage"/>
                    <img src="https://assets.codepen.io/5647096/Delorean.png" alt="El Delorean, El Doc y Marti McFly"/>
                </div>
                <div className="notFoundText">
                    <h1 className="notFoundH1">404</h1>
                    <h2 className="notFoundH2">PAGE NOT FOUND</h2>
                    <h3 className="notFoundH3">BACK TO HOME?</h3>
                    <NavLink to="/courses" className="notFoundA">YES</NavLink>
                    <a className="notFoundA" href="https://www.youtube.com/watch?v=G3AfIvJBcGo">NO</a>
                </div>
            </section>
            </div>
        </div>
    );
};

export default NotFoundPage;