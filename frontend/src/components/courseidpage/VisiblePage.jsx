import React, {useEffect} from 'react';
import {Button} from "react-bootstrap";
import {NavLink} from "react-router-dom";
import "./visiblepage.css";
import VideoItem from "./VideoItem";
import axios from "axios";
import MyRate from "./rating/MyRate";

const VisiblePage = ({
                         courseInfo, videos, setVideoURL, setPlayerStatus, username,
                         setAuthorEditMode, subscribeInfo, setSubscribeInfo
                     }) => {

    useEffect(() => {
        if (username != null && courseInfo.name != null && username != courseInfo.author) {
            axios({
                method: "get",
                url: "http://localhost:8080/subscribe",
                params:
                    {
                        user_username: username,
                        course_name: courseInfo.name
                    },
                headers: {"Content-Type": "multipart/form-data"},
                withCredentials: true
            }).then((response) => {
                setSubscribeInfo(response.data);
            });
        }
    }, [courseInfo])

    const subscribe = () => {
        axios({
            method: "post",
            url: "http://localhost:8080/subscribe",
            data:
                {
                    user_username: username,
                    course_name: courseInfo.name
                },
            withCredentials: true
        }).then((response) => {
            setSubscribeInfo(response.data);
        })
    }

    const rateCourse = (rating) => {
        axios({
            method: "patch",
            url: "http://localhost:8080/courses/" + courseInfo.id + "/rate",
            params:
                {
                    username: username,
                    rating: rating
                },
            withCredentials: true
        }).then((response) => {
            window.location.reload();
        })
    }

    return (
        <div className="course">
            <div className="main">

                {
                    courseInfo.author == username ?
                        <Button className="editPageButton" variant="outline-info"
                                onClick={() => setAuthorEditMode(true)}>
                            &#x270E;
                        </Button>
                        :
                        null
                }

                <div className="imageBlock">
                    <img width={200} height={"auto"} className="image"
                         src={"http://localhost:3000/courses/" + courseInfo.image_url}></img>
                    <div style={{fontSize: 22}}>{courseInfo.price} ??????.</div>

                    {
                        username != courseInfo.author && username != null ?
                            !subscribeInfo ?
                                <Button variant="outline-success" className="buyButton"
                                        onClick={subscribe}>????????????</Button>
                                :
                                <div className="subBody">
                                    <Button variant="outline-warning" disabled>??????????????!</Button>
                                    <MyRate rate={subscribeInfo.rate} rateCourse={rateCourse} subscribeInfo={subscribeInfo}/>
                                </div>
                            :
                            null
                    }
                </div>
                <div className="textBlock">

                    <div className="textBlockHead">
                        <h2 className="courseName">{courseInfo.name}</h2>
                        <div>{Math.round(courseInfo.rating * 100)/100}&#x2730; / {courseInfo.number_votes} ??????????.</div>
                    </div>

                    <div className="textBlockBody">
                        <div className="textBlockDiv">
                            &#x25C6; ??????????: <NavLink to={'/author/' + courseInfo.author}>{courseInfo.author}</NavLink>
                        </div>
                        <div className="textBlockDiv">
                            &#x25C6; ??????????????????: {courseInfo.categories.map((name) => '[' + name + ']')}
                        </div>
                        <div className="textBlockDiv">
                            &#x25C6; ???????? ????????????????????: {courseInfo.release_date}
                        </div>
                        <div className="textBlockDiv">
                            &#x25C6; ???????? ???????????????????? ????????????????????: {courseInfo.update_date}</div>
                        <div className="textBlockDiv">
                            &#x25C6; ???????????????????? ??????????: {courseInfo.video_count}
                        </div>
                    </div>

                </div>
            </div>

            <div className="descriptionBlock">
                <h4 style={{textAlign: "center"}}>????????????????</h4>
                <p style={{marginLeft: 10, marginRight: 10}}>{courseInfo.description}</p>
            </div>

            <div className="visiblePageLine"></div>

            <div>
                {
                    videos.map((video) =>
                        <VideoItem key={videos.indexOf(video)} video={video}
                                   setPlayerStatus={setPlayerStatus} setVideoURL={setVideoURL}/>
                    )
                }
            </div>

        </div>
    );
};

export default VisiblePage;