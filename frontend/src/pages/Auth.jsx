import React, {useContext, useState} from 'react';
import {Button, Card, Container, Form, Row} from "react-bootstrap";
import {NavLink, useHistory, useLocation} from "react-router-dom";
import axios from "axios";

import MyModal from "../components/MyModal";
import {AuthContext} from "../context/authContext";
import validation from "../components/validation";

const Auth = () => {

        const {setIsAuth, setRole, setUsername} = useContext(AuthContext);

        const location = useLocation();
        const history = useHistory();
        const isLogin = location.pathname === '/login';

        const [name, setName] = useState('');
        const [usernameInput, setUsernameInput] = useState('');
        const [email, setEmail] = useState('');
        const [password, setPassword] = useState('');
        const [errors, setErrors] = useState({});

        const [isModal, setIsModal] = useState(false);

        const register = async (e) => {
            setRegError('');
            e.preventDefault();
            setErrors(validation({email: email, password: password, name: name, username: usernameInput}));
            if (Object.keys(errors).length === 1) {
                await axios.post('http://localhost:8080/api/v1/registration', {
                    name: name,
                    username: usernameInput,
                    password: password,
                    email: email
                }).then((response) => {
                    setIsModal(true); // модальное окно
                }).catch(function (error) {
                    setRegError("Имя пользователя и/или почта уже зарегистрирована");
                });
            }
        }

        const login = async (e) => {
            e.preventDefault();
            setErrors(validation({email: email, password: password}));
            if (Object.keys(errors).length === 3) {
                await axios({
                    method: "post",
                    url: "http://localhost:8080/login",
                    data: {email: email, password: password},
                    headers: {"Content-Type": "multipart/form-data"},
                    withCredentials: true
                }).then((response) => {
                    axios({
                        method: "get",
                        url: "http://localhost:8080/user",
                        withCredentials: true
                    }).then((response => {
                        if (response.data.name) {
                            setIsAuth({type: 'setAuth', payload: true});
                            localStorage.setItem('auth', true);
                            setRole(response.data.role);
                            localStorage.setItem('role', response.data.role);
                            setUsername(response.data.username);
                            localStorage.setItem('username', response.data.username);

                            history.push("/");
                        }
                    }));
                });
                axios({
                    method: "get",
                    url: "http://localhost:8080/user",
                    withCredentials: true
                });
            }

        }

        const [regError, setRegError] = useState('');

        return (
            <Container
                className="d-flex justify-content-center align-items-center"
                style={{height: window.innerHeight - 55}}
            >
                <Card style={{width: 600}} className="p-5">
                    <h2 className="m-auto">{isLogin ? 'Авторизация' : 'Регистрация'}</h2>
                    <Form className="d-flex flex-column">
                        {isLogin ? null :
                            <div>
                                {regError && <p className="error">{regError}</p>}
                                {errors.name && <p className="error">{errors.name}</p>}
                                <Form.Control
                                    className="mt-3"
                                    placeholder="Введите ваше имя..."
                                    value={name}
                                    onChange={e => setName(e.target.value)}
                                />
                                {errors.username && <p className="error">{errors.username}</p>}
                                <Form.Control
                                    className="mt-3"
                                    placeholder="Введите ваш псевдоним..."
                                    value={usernameInput}
                                    onChange={e => setUsernameInput(e.target.value)}
                                />
                            </div>
                        }
                        {errors.email && <p className="error">{errors.email}</p>}
                        <Form.Control
                            className="mt-3"
                            placeholder="Введите ваш email..."
                            value={email} onChange={e => setEmail(e.target.value)}
                        />
                        {errors.password && <p className="error">{errors.password}</p>}
                        <Form.Control
                            className="mt-3"
                            placeholder="Введите ваш пароль..."
                            value={password}
                            onChange={e => setPassword(e.target.value)}
                            type="password"
                        />
                        <Row className="d-flex justify-content-between mt-3 pl-3 pr-3">
                            {isLogin ?
                                <div>
                                    Нет аккаунта? <NavLink to={'/registration'}>Зарегистрируйся!</NavLink>
                                </div>
                                :
                                <div>
                                    Есть аккаунт? <NavLink to={'/login'}>Войдите!</NavLink>
                                </div>
                            }
                            <Button
                                variant={"outline-success"}
                                onClick={isLogin ? login : register}
                            >
                                {isLogin ? 'Войти' : 'Регистрация'}
                            </Button>

                            <MyModal
                                visible={isModal}
                                title='Регистрация почти завершена'
                                content={<p>Осталось подтвердить почту {email}, на которую было отправлено письмо с
                                    ссылкой</p>}
                                onClose={() => {
                                    setIsModal(false);
                                    history.push('/login')
                                }}
                            />
                        </Row>
                    </Form>
                </Card>
            </Container>
        );
    }
;

export default Auth;