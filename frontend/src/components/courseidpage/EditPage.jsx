import React, {createRef, useEffect, useState} from 'react';
import {Button, Form} from "react-bootstrap";
import axios from "axios";
import "./editpage.css";
import VideoItem from "./VideoItem";

const EditPage = ({courseInfo, videos, setVideos, setVideoURL, setPlayerStatus, setAuthorEditMode}) => {

    const [newCourseInfo, setNewCourseInfo] = useState({});
    const [newVideo, setNewVideo] = useState({});
    const fileInput_image = createRef();
    const fileInput_video = createRef();

    const [error, setError] = useState('');
    const [error_video, setError_video] = useState('');

    useEffect(() => {
        setNewCourseInfo({
            price: courseInfo.price,
            description: courseInfo.description
        })
    }, [courseInfo]);

    const imageSubmit = (event) => {
        event.preventDefault();
        axios({
            method: "patch",
            url: "http://localhost:8080/courses/" + courseInfo.id + "/photo",
            data: {file: fileInput_image.current.files[0]},
            headers: {'content-type': 'multipart/form-data'},
            withCredentials: true
        }).then((response) => {
            window.location.reload();
        });
    }

    const videoSubmit = () => {
        if (newVideo.name === undefined || newVideo.name.length < 5 || newVideo.status === undefined ||
            fileInput_video.current.files[0] === undefined) {
            setError_video("Incorrect data");
        } else {
            setError_video('');
            axios({
                method: "post",
                url: "http://localhost:8080/video",
                data:
                    {
                        name: newVideo.name,
                        status: newVideo.status,
                        course_id: courseInfo.id
                    },
                withCredentials: true
            }).then((response) => {
                axios({
                    method: "patch",
                    url: "http://localhost:8080/video/" + response.data + "/video",
                    data: {file: fileInput_video.current.files[0]},
                    headers: {'content-type': 'multipart/form-data'},
                    withCredentials: true
                }).then((response) => {
                    window.location.reload();
                })
            });
        }

    }

    const updateCourse = () => {
        if (newCourseInfo.price <= 0 || newCourseInfo.description === '' || newCourseInfo.description.length < 5) {
            setError("Incorrect data");
        } else
            axios({
                method: "patch",
                url: "http://localhost:8080/courses/" + courseInfo.id,
                params:
                    {
                        price: newCourseInfo.price,
                        description: newCourseInfo.description
                    },
                headers: {'content-type': 'multipart/form-data'},
                withCredentials: true
            }).then((response) => {
                window.location.reload();
            });
    }

    const releaseCourse = () => {
        axios({
            method: "patch",
            url: "http://localhost:8080/courses/" + courseInfo.id + "/release",
            headers: {'content-type': 'multipart/form-data'},
            withCredentials: true
        }).then((response) => {
            window.location.reload();
        });
    }

    const freezeCourse = () => {
        axios({
            method: "patch",
            url: "http://localhost:8080/courses/" + courseInfo.id + "/freeze",
            headers: {'content-type': 'multipart/form-data'},
            withCredentials: true
        }).then((response) => {
            window.location.reload();
        });
    }

    const deleteVideo = (id) => {
        axios({
            method: "delete",
            url: "http://localhost:8080/video/" + courseInfo.id + "/video",
            params:
                {
                    video_id: id
                },
            headers: {'content-type': 'multipart/form-data'},
            withCredentials: true
        }).then((response) => {
            window.location.reload();
        });
    }

    return (
        <div className="course">

            <Button className="editPageButton" variant="outline-info"
                    onClick={() => setAuthorEditMode(false)}>&#x270E;</Button>

            <h2 className="editPageHeader">Редактирование курса</h2>
            <div className="editPageLine"></div>

            <div className="imageUploadCourse">
                <img width={120} height={120} className="imageClass"
                     src={"http://localhost:3000/courses/" + courseInfo.image_url}
                     alt="image"/>

                <Form onSubmit={imageSubmit} style={{marginLeft: 10}}>
                    <p>Загрузить новую аватарку:</p>
                    <Form.Control
                        multiple
                        type="file"
                        accept=".jpg, .jpeg, .png"
                        className="mt-3 imageUpload"
                        ref={fileInput_image}
                    />
                    <p>
                        <Button variant="outline-dark" component="span" type="submit">Upload</Button>
                    </p>
                </Form>
                {/*<a style={{color: "orange"}} href="#" onClick={() => deleteImage()}>Вернуть стандартную фотку</a>*/}
            </div>

            <div>
                <div className="editPageBody">
                    <p>Цена:</p>
                    <Form.Control
                        className="editPageForm"
                        placeholder="Новая цена..."
                        defaultValue={newCourseInfo.price}
                        value={newCourseInfo.price} onChange={e => setNewCourseInfo({price: e.target.value})}
                    />
                </div>

                <div className="editPageBody">
                    <p>Описание: </p>
                    <Form.Control
                        className="editPageForm"
                        style={{width: "100%"}} as="textarea" rows={3}
                        placeholder="Новое название..."
                        defaultValue={newCourseInfo.description}
                        value={newCourseInfo.description}
                        onChange={e => setNewCourseInfo({description: e.target.value})}
                    />
                </div>

                <div style={{color: "red", fontWeight: "bold", textAlign: "center"}}>{error}</div>

                <div className="editPageButtons">
                    <div className="editPageButtonsDiv">
                        <Button disabled={!courseInfo.is_visible} variant="outline-danger" onClick={freezeCourse}>
                            Заморозить
                        </Button>
                    </div>
                    <div className="editPageButtonsDiv">
                        <Button variant="outline-primary" onClick={updateCourse}>
                            Сохранить
                        </Button>
                    </div>
                    <div className="editPageButtonsDiv">
                        <Button disabled={courseInfo.is_visible} variant="outline-success" onClick={releaseCourse}>
                            Опубликовать!
                        </Button>
                        {/*<div id="editPageButtonsText">Скрыть курс будет невозможно после публикации!</div>*/}
                    </div>

                </div>

                <div className="editPageLine" style={{marginTop: 5}}></div>


                <div className={"editPageVideos"}>
                    <p style={{textAlign: "center", textDecoration: "underline"}}>Текущие видео: </p>
                    {
                        videos.map((video) =>
                            <VideoItem key={videos.indexOf(video)} video={video}
                                       setPlayerStatus={setPlayerStatus} setVideoURL={setVideoURL}
                                       deleteVideo={deleteVideo} courseInfo={courseInfo}/>
                        )
                    }

                    <div className="editPageLine" style={{marginTop: 5}}></div>

                    <div>
                        <p style={{textAlign: "center", textDecoration: "underline"}}>Добавление видео</p>
                        <div style={{marginLeft: 10}}>

                            <p>Загрузить новую аватарку:</p>
                            <Form.Control
                                style={{width: 320}}
                                multiple
                                type="file"
                                accept="video/mp4,video/x-m4v,video/*"
                                className="mt-3"
                                ref={fileInput_video}
                            />

                            <p>Название: </p>
                            <Form.Control
                                style={{width: 500}}
                                placeholder=""
                                value={newVideo.name}
                                onChange={e => setNewVideo({name: e.target.value, status: newVideo.status})}
                            />

                            <p>Статус: </p>
                            <input type="radio"
                                   name="status"
                                   value="free"
                                   onChange={e => setNewVideo({status: e.target.value, name: newVideo.name})}
                            />
                            <label>Бесплатное видео</label>
                            <br/>
                            <input type="radio"
                                   name="status"
                                   value="premium"
                                   onChange={e => setNewVideo({status: e.target.value, name: newVideo.name})}
                            />
                            <label>Доступ по подписке</label>

                            <div style={{color: "red", fontWeight: "bold", textAlign: "center"}}>{error_video}</div>

                            <p>
                                <Button variant="outline-dark" component="span" onClick={videoSubmit}>Upload</Button>
                            </p>
                        </div>
                    </div>

                </div>

            </div>

        </div>
    );
};

export default EditPage;