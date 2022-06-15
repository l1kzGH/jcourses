import React, {useContext} from 'react';
import {Redirect, Route, Switch} from "react-router-dom";
import Auth from "../pages/Auth";
import Courses from "../pages/Courses";
import Profile from "../pages/Profile";
import {AuthContext} from "../context/authContext";
import ControlPanel from "../pages/ControlPanel";
import CourseIdPage from "../pages/CourseIdPage";
import AuthorPage from "../pages/AuthorPage";
import NotFoundPage from "../pages/notfound/NotFoundPage";

const AppRouter = () => {

    const {isAuth, role} = useContext(AuthContext);
    console.log(isAuth); //todo редирект при обновлении на isAuth страницах

    return (
        <Switch>
            <Route exact path='/courses' key={0}>
                <Courses/>
            </Route>
            <Route exact path='/courses/:id' key={1}>
                <CourseIdPage/>
            </Route>
            <Route exact path='/author/:name' key={2}>
                <AuthorPage/>
            </Route>
            {!isAuth ?
                [<Route path={'/login'} key={3}>
                    <Auth/>
                </Route>,
                    <Route path='/registration' key={4}>
                        <Auth/>
                    </Route>]
                : null
            }
            {isAuth ?
                <Route path='/profile' key={5}>
                    <Profile/>
                </Route> : null}
            {isAuth && role == "ADMIN" || role == "AUTHOR" ?
                <Route path='/control' key={6}>
                    <ControlPanel/>
                </Route> : null
            }
            <Route exact path='/error' key={7}>
                <NotFoundPage/>
            </Route>
            <Redirect to='/courses'/>
        </Switch>
    );
};

export default AppRouter;