import React, {useContext, useEffect, useState} from 'react';
import {Button, Container, Nav, Navbar} from 'react-bootstrap';
import {NavLink, useHistory} from "react-router-dom";
import {AuthContext} from "../context/authContext";
import axios from "axios";
import "./navbar.css";
import NotifyItem from "./notification/NotifyItem";

const NavBar = () => {
    const history = useHistory()
    const {isAuth, setIsAuth, role, username} = useContext(AuthContext);

    const [notifies, setNotifies] = useState([]);

    useEffect(() => {
        if (username != undefined && username != null)
            axios({
                method: "get",
                url: "http://localhost:8080/notification",
                params:
                    {
                        username: username
                    },
                headers: {"Content-Type": "multipart/form-data"},
                withCredentials: true
            }).then((response) => {
                console.log(response.data);
                setNotifies(response.data);
            })
    }, [username]);

    const logout = () => {
        setIsAuth({type: "setAuth", payload: false});
        localStorage.removeItem('auth');
        axios({
            method: "get",
            url: "http://localhost:8080/logout",
            withCredentials: true
        });
        window.location.reload();
    }

    const showNotifies = () => {
        let nots = document.getElementsByClassName("notifyMain");
        for (let i = 0; i < nots.length; i++) {
            if(nots[i].style.display === "none"){
                nots[i].style.display = "block"
            } else nots[i].style.display = "none";
        }
    }

    const hideNotifies = () => {
        let nots = document.getElementsByClassName("notifyMain");
        for (let i = 0; i < nots.length; i++) {
            if(nots[i].style.display === "block"){
                nots[i].style.display = "none"
            }
        }
    }

    return (
        <Navbar bg="dark" variant="dark">
            <Container>
                <NavLink to={'/courses'}
                         style={{color: "white", textDecoration: "none", fontSize: 20}}>JCourses</NavLink>
                <Nav style={{color: "white"}}>
                    <Nav style={{color: 'white'}}>
                        {!isAuth ?
                            <Button variant={"outline-primary"}
                                    onClick={() => history.push('/login')}>Авторизация</Button>
                            :
                            [
                                <div onMouseEnter={showNotifies} onMouseLeave={hideNotifies} className="notifyButton">
                                    <Button variant="outline-light">
                                        &#128276;
                                    </Button>
                                    {
                                        notifies.map((notify, index) => <NotifyItem key={index} notify={notify}
                                                                                    index={index}/>)
                                    }
                                </div>,

                                role == "AUTHOR" || role == "ADMIN" ?
                                    <Button style={{marginRight: 20}} key={0} variant={"outline-warning"}
                                            onClick={() => history.push('/control')}>Панель управления</Button> : null,
                                <Button style={{marginRight: 20}} key={1} variant={"outline-light"}
                                        onClick={() => history.push('/profile')}>Профиль</Button>,
                                <Button key={2} variant={"outline-danger"}
                                        onClick={logout}>Выйти</Button>
                            ]
                        }
                    </Nav>
                </Nav>
            </Container>
        </Navbar>
    );
};

export default NavBar;