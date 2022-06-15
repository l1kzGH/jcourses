import React, {createRef, useContext, useState} from 'react';
import {Button, Form} from "react-bootstrap";
import {NavLink} from "react-router-dom";
import "./editform.css";
import axios from "axios";
import {AuthContext} from "../../context/authContext";

const EditForm = ({userInfo, setStatus}) => {

    const {setIsAuth} = useContext(AuthContext);
    const [newUserInfo, setNewUserInfo] = useState({});
    const fileInput = createRef();
    const [error, setError] = useState();

    const submit = () => {

        //validation
        if ((newUserInfo.username === undefined && newUserInfo.name === undefined) ||
            (newUserInfo.username != undefined && newUserInfo.username.length < 4) ||
            (newUserInfo.name != undefined && newUserInfo.name.length < 2)) {
            setError("Incorrect data");
        } else
            axios({
                method: "patch",
                url: "http://localhost:8080/user/" + userInfo.id,
                params:
                    {
                        name: newUserInfo.name,
                        username: newUserInfo.username
                    },
                withCredentials: true
            }).then((response) => {
                console.log(response.data);
                if (newUserInfo.username != null) {
                    setIsAuth({type: "setAuth", payload: false});
                    localStorage.removeItem('auth');
                    axios({
                        method: "get",
                        url: "http://localhost:8080/logout",
                        withCredentials: true
                    });
                }
                window.location.reload();
            });
    }

    const imageSubmit = (event) => {
        event.preventDefault();
        axios({
            method: "patch",
            url: "http://localhost:8080/user/" + userInfo.id + "/photo",
            data: {file: fileInput.current.files[0]},
            headers: {'content-type': 'multipart/form-data'},
            withCredentials: true
        });
        window.location.reload();
    }

    const deleteImage = () => {
        axios({
            method: "get",
            url: "http://localhost:8080/user/" + userInfo.id + "/photo/delete",
            withCredentials: true
        });
        window.location.reload();
    }

    return (
        <div className="header header-primary">
            <div>
                <img width={100} height={100} className="imageClass"
                     src={"http://localhost:3000/profiles/" + userInfo.image_url}
                     alt="image"/>
            </div>

            <Form onSubmit={imageSubmit}>
                <p>Загрузить новую аватарку:</p>
                <Form.Control
                    multiple
                    type="file"
                    accept=".jpg, .jpeg, .png"
                    className="mt-3 imageUpload"
                    ref={fileInput}
                />
                <p><Button variant="outline-dark" component="span"
                           type="submit">Upload</Button></p>
            </Form>
            <a style={{color: "orange"}} href="#" onClick={() => deleteImage()}>Вернуть стандартную фотку</a>

            <div className="description">
                <div className="editLine"></div>
                <p>Имя: {userInfo.name}</p>
                <Form.Control
                    className="mt-3 nameFormControl"
                    placeholder="Введите новое имя"
                    value={newUserInfo.name} onChange={e => setNewUserInfo({name: e.target.value})}
                />
                <p>Псевдоним: {userInfo.username}</p>
                <Form.Control
                    className="mt-3 nameFormControl"
                    placeholder="Введите новый псевдоним"
                    value={newUserInfo.username} onChange={e => setNewUserInfo({username: e.target.value})}
                />
                <p style={{color: "orangered", fontSize: 15}}>При изменении псевдонима Вам придется перезайти на
                    сайт</p>
            </div>

            <Button variant={"outline-primary"} onClick={submit}>Подтвердить</Button>
            <div><NavLink to={'/profile'} onClick={() => setStatus(true)}>Вернуться без изменений</NavLink></div>
            <div style={{color: "red", fontWeight: "bold"}}>{error}</div>

        </div>
    );
};

export default EditForm;