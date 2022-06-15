import React, {useContext, useEffect, useState} from 'react';
import axios from "axios";
import {Button, Card, Container, Form, Row, ToggleButton} from "react-bootstrap";
import {AuthContext} from "../context/authContext";
import "../styles/controlpanelcourse.css";
import {useHistory} from "react-router-dom";

const ControlPanel = () => {

    const history = useHistory();
    const {role, username} = useContext(AuthContext);
    const [menu, setMenu] = useState(null);
    const [response, setResponse] = useState({text: '', color: ''});

    const [count, setCount] = useState(20);
    const [page, setPage] = useState(1);

    const [name, setName] = useState('');
    const [courseCategories, setCourseCategories] = useState([]);
    const [price, setPrice] = useState(0);
    const [description, setDescription] = useState('');

    const [courses, setCourses] = useState([]);
    const [error, setError] = useState('');

    useEffect(() => {
        let author = null;
        if (role == "AUTHOR") author = username;
        axios({
            method: "get",
            url: "http://localhost:8080/courses",
            params:
                {
                    "count": count,
                    "page": page,
                    "author": author,
                    "visibility": "true"
                },
            withCredentials: true
        }).then((response) => {
            setCourses(response.data);
        });
    }, [])

    let categoriesList;
    useEffect(() => {
        axios({
            method: "get",
            url: "http://localhost:8080/category",
            withCredentials: true
        }).then((response => {
            categoriesList = Array.from({length: response.data.length}).map((_, idx) => {
                return {name: response.data[idx].name, id: idx + 1, clicked: false};
            });
            setCategories(categoriesList);
        }));
    }, []);

    const [categories, setCategories] = useState(categoriesList);

    function handleButtonClick(buttonId) {
        const nextState = categories.map(button => {
            if (button.id !== buttonId) {
                return button;
            }
            return {...button, clicked: !button.clicked};
        });

        let mass = [];
        for (let i = 0; i < nextState.length; i++) {
            if (nextState[i].clicked == true) mass.push(nextState[i].name);
        }
        setCourseCategories(mass);
        setCategories(nextState);
        //console.log(nextState);
        //console.log(mass);
    }


    const addCourse = () => {
        if ((name === '' || price <= 0 || description === '') || (name.length < 5)) {
            setError("Недопустимые данные")
        } else
            axios({
                method: "post",
                url: "http://localhost:8080/courses/create",
                data:
                    {
                        "author": username,
                        "name": name,
                        "category_name": courseCategories,
                        "price": price,
                        "description": description
                    },
                withCredentials: true
            }).then((response) => {
                console.log(response.data);
                setResponse({text: response.data, color: 'green'});
                window.location.reload();
            }).catch((error) => {
                setResponse({text: error.response.data, color: 'red'});
            });
    }

    const deleteCourse = (name) => {
        axios({
            method: "delete",
            url: "http://localhost:8080/courses/" + name,
            withCredentials: true
        }).then((response) => {
            setResponse({text: response.data.body, color: 'green'});
            window.location.reload();
        })
    }

    const freezeCourse = (id) => {
        axios({
            method: "patch",
            url: "http://localhost:8080/courses/" + id + "/freeze",
            headers: {'content-type': 'multipart/form-data'},
            withCredentials: true
        }).then((response) => {
            window.location.reload();
        })
    }

    const addCategory = (name) => {
        axios({
            method: "post",
            url: "http://localhost:8080/category/create",
            data: {"name": name},
            withCredentials: true
        }).then((response) => {
            setResponse({text: response.data.body, color: 'green'});
            window.location.reload();
        }).catch((error) => {
            setResponse({text: error.response.data, color: 'red'});
        })
    }

    return (
        <div style={{textAlign: 'center'}}>
            <h1 style={{color: "white"}}>Панель управления</h1>

            {role == "ADMIN" ?
                <Button variant={"outline-primary"}
                        onClick={() => {
                            setResponse({text: '', color: 'black'});
                            if (menu != "createCategory")
                                setMenu("createCategory");
                            else setMenu();
                        }}>Создать категорию</Button>
                : null
            }
            <br/>
            {role == "ADMIN" || role == "AUTHOR" ?
                <Button variant={"outline-primary"}
                        style={{marginTop: 10}}
                        onClick={() => {
                            setResponse({text: '', color: 'black'});
                            if (menu != "createCourse")
                                setMenu("createCourse");
                            else setMenu();
                        }}>Создать курс</Button>
                : null
            }
            <br/>
            {role == "ADMIN" || role == "AUTHOR" ?
                <Button variant={"outline-danger"}
                        style={{marginTop: 10}}
                        onClick={() => {
                            setResponse({text: '', color: 'black'});
                            if (menu != "deleteCourse")
                                setMenu("deleteCourse");
                            else setMenu();
                        }}>Показать курсы</Button>
                : null
            }

            {
                menu == "createCourse" ?
                    <Container
                        className="d-flex justify-content-center align-items-top"
                        style={{flexDirection: "column"}}
                    >
                        <Card style={{width: 600}} className="p-2">
                            <Form className="d-flex flex-column">
                                <h2>Новый курс</h2>

                                <Form.Group className="mb-3">
                                    <Form.Label>Название</Form.Label>
                                    <Form.Control value={name} placeholder="Минимум 5 букв" onChange={e => setName(e.target.value)}/>
                                </Form.Group>

                                <Form.Group className="mb-3">
                                    <Form.Label>Цена</Form.Label>
                                    <Form.Control value={price} onChange={e => setPrice(e.target.value)}/>
                                </Form.Group>

                                <Form.Group className="mb-1">
                                    <Form.Label>Категории</Form.Label>
                                    <Row>
                                        {categories.map((category) =>
                                            <ToggleButton onChange={() => {
                                                handleButtonClick(category.id)
                                            }}
                                                          variant={"outline-info"}
                                                          key={category.id}
                                                          id={category.id}
                                                          type="checkbox"
                                                          value="1"
                                                          checked={category.clicked}
                                                          style={{width: "auto", marginRight: 5, marginLeft: 5}}>
                                                {category.name}
                                            </ToggleButton>
                                        )}
                                    </Row>
                                </Form.Group>

                                <Form.Group className="mb-3">
                                    <Form.Label>Описание</Form.Label>
                                    <Form.Control as="textarea" rows={3} value={description}
                                                  onChange={e => setDescription(e.target.value)}/>
                                </Form.Group>

                                <div style={{color: response.color}}>
                                    {response.text}
                                </div>
                                <div style={{color: "red", fontWeight: "bold"}}>{error}</div>

                                <Button variant={"outline-success"} onClick={() => {
                                    addCourse()
                                }}>
                                    Продолжить
                                </Button>
                            </Form>
                        </Card>
                    </Container>

                    : null
            }

            {
                menu == "deleteCourse" ?
                    <div style={{color: "white"}}>

                        <div className="pwbHead">
                            <div className="primerWithoutBorder">
                                Название
                            </div>
                            <div className="primerWithoutBorder">
                                Категории
                            </div>
                            <div className="primerWithoutBorder">
                                Автор
                            </div>
                            <div className="primerWithoutBorder">

                            </div>
                        </div>

                        {courses.map((course) =>
                            <div className="primerHead" key={courses.indexOf(course)}>
                                <div className="primer">
                                    {course.name}
                                </div>
                                <div className="primer">
                                    {course.category_name.map((name) => '[' + name + ']')}
                                </div>
                                <div className="primer">
                                    {course.author}
                                </div>

                                <div style={{color: response.color}}>
                                    {response.text}
                                </div>

                                <Button variant={"outline-info"} onClick={() => history.push("/courses/" + course.id)}>
                                    Перейти
                                </Button>

                                <Button variant={"outline-danger"} onClick={() => {
                                    freezeCourse(course.id)
                                }}>
                                    Заморозить
                                </Button>
                            </div>
                        )}

                    </div>

                    : null
            }

            {
                menu == "createCategory" ?
                    <div>
                        <Container
                            className="d-flex justify-content-center align-items-top"
                        >
                            <Card style={{width: 600}} className="p-2">
                                <Form className="d-flex flex-column">
                                    <h2>Новая категория</h2>

                                    <Form.Group className="mb-3">
                                        <Form.Label>Название</Form.Label>
                                        <Form.Control value={name} onChange={e => setName(e.target.value)}/>
                                    </Form.Group>

                                    <div style={{color: response.color}}>
                                        {response.text}
                                    </div>

                                    <Button variant={"outline-success"} onClick={() => {
                                        addCategory(name)
                                    }}>
                                        Добавить
                                    </Button>
                                </Form>
                            </Card>
                        </Container>
                    </div>
                    : null
            }

        </div>
    );
};

export default ControlPanel;