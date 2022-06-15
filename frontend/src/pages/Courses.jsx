import React, {useEffect, useState} from 'react';
import axios from "axios";
import CourseList from "../components/courses/CourseList";
import Sort from "../components/courses/Sort";
import "../styles/coursespage.css";
import {Button} from "react-bootstrap";

const Courses = () => {

    const [courses, setCourses] = useState([]);

    const [count, setCount] = useState(2);
    const [page, setPage] = useState(1);
    const [selectedSort, setSelectedSort] = useState(null);
    const [search, setSearch] = useState(null);

    const [maxPages, setMaxPages] = useState(2);
    const [searchValue, setSearchValue] = useState('');

    const [buttonPage, setButtonPage] = useState(1);

    useEffect(() => {
        axios({
            method: "get",
            url: "http://localhost:8080/courses",
            params:
                {
                    "count": count,
                    "page": page,
                    "sortName": selectedSort,
                    "search": search,
                    "visibility": true
                },
            withCredentials: true
        }).then((response) => {
            setCourses(response.data);

            if (page > 1 && page < maxPages - 2)
                setButtonPage(page - 1);
            else if (page == maxPages && maxPages > 4)
                setButtonPage(page - 4);
        });
    }, [page, selectedSort, search])

    useEffect(() => {
        axios({
            method: "get",
            url: "http://localhost:8080/courses/count/" + count,
            params:
                {
                    "search": search,
                    "visibility": true
                },
            withCredentials: true
        }).then((response) => {
            setMaxPages(response.data);
        });
        setPage(1);
    }, [search])

    const sortCourses = (sort) => {
        setSelectedSort(sort);
        console.log(sort);
    }

    const searchCourses = (search) => {
        setSearch(search);
        console.log(search);
    }

    return (

        <div>

            <form className="searchBox">
                <input className="myInput" placeholder="Искать здесь..." type="search"
                       value={searchValue}
                       onChange={e => setSearchValue(e.target.value)}/>
                <Button className="myButton" variant={"outline-success"} onClick={() => searchCourses(searchValue)}>
                    Поиск
                </Button>
            </form>

            <div className={'sort'}>
                <div className={'sort-top'}>
                    Сортировка по
                </div>
                <Sort
                    value={selectedSort ? selectedSort : ''}
                    defaultValue="-"
                    onChange={sortCourses}
                    options={[
                        {value: 'name', name: 'Названию'},
                        {value: 'price', name: 'Стоимости'},
                        {value: 'rating', name: 'Рейтингу'}
                    ]}
                />
            </div>

            <div className={"bodyCourses"}>
                <CourseList courses={courses} title="Курсы"/>
            </div>

            {maxPages > 0 ?
                <div className={"buttons"}>
                    {
                        Array.apply(0, Array(maxPages < 5 ? maxPages - 1 : 4)).map((_, idx) =>
                            <Button
                                className={"buttonPage"}
                                key={buttonPage + idx}
                                variant={buttonPage + idx == page ? "outline-warning" : "outline-primary"}
                                onClick={() => setPage(buttonPage + idx)}>
                                {buttonPage + idx}
                            </Button>
                        )
                    }

                    <Button
                        className={"buttonPage"}
                        key={maxPages}
                        variant={maxPages == page ? "outline-warning" : "outline-primary"}
                        onClick={() => setPage(maxPages)}>
                        {maxPages}
                    </Button>
                </div>
                : null
            }

        </div>


    );
};

export default Courses;