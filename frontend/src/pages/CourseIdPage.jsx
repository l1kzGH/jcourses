import React, {useContext, useEffect, useState} from 'react';
import axios from "axios";
import "../styles/courseidpage.css";
import {useHistory} from "react-router-dom";
import VisiblePage from "../components/courseidpage/VisiblePage";
import EditPage from "../components/courseidpage/EditPage";
import {AuthContext} from "../context/authContext";
import MyReactPlayer from "../components/MyReactPlayer";

const CourseIdPage = () => {

    const {username} = useContext(AuthContext);
    const history = useHistory();

    const [courseInfo, setCourseInfo] = useState({
        categories: []
    });
    const [authorEditMode, setAuthorEditMode] = useState(false);

    const [videos, setVideos] = useState([]);
    const [playerStatus, setPlayerStatus] = useState(false);
    const [videoURL, setVideoURL] = useState('');

    const [subscribeInfo, setSubscribeInfo] = useState('');

    useEffect(() => {
        axios({
            method: "get",
            url: "http://localhost:8080" + window.location.pathname,
            withCredentials: true
        }).then((response) => {
            setCourseInfo({
                id: response.data.id,
                author: response.data.author,
                name: response.data.name,
                categories: [response.data.category_name],
                rating: response.data.rating,
                number_votes: response.data.number_votes,
                price: response.data.price,
                image_url: response.data.image_url,
                description: response.data.description,
                video_count: response.data.video_count,
                release_date: response.data.release_date,
                update_date: response.data.update_date,
                is_visible: response.data._visible
            })
        }).catch((error) => {
            history.push('/error');
        })
    }, [])

    useEffect(() => {
        if (courseInfo.id != null) {
            let status = "free";
            if (subscribeInfo != '' && username != null || username == courseInfo.author) {
                status = null;
            }

            axios({
                method: "get",
                url: "http://localhost:8080/video/course/" + courseInfo.id,
                params:
                    {
                        status: status
                    },
                withCredentials: true
            }).then((response) => {
                setVideos(response.data);
            });
        }

    }, [courseInfo, subscribeInfo])

    return (
        <div>

            {
                (!authorEditMode) && (courseInfo.is_visible || courseInfo.is_visible == null) ?
                    <VisiblePage courseInfo={courseInfo} videos={videos}
                                 setPlayerStatus={setPlayerStatus} setVideoURL={setVideoURL}
                                 username={username} setAuthorEditMode={setAuthorEditMode}
                                 subscribeInfo={subscribeInfo} setSubscribeInfo={setSubscribeInfo}/>
                    :
                    <div>
                        {
                            courseInfo.author == username ?
                                <EditPage courseInfo={courseInfo} videos={videos} setVideos={setVideos}
                                          setPlayerStatus={setPlayerStatus} setVideoURL={setVideoURL}
                                          setAuthorEditMode={setAuthorEditMode}
                                />
                                :
                                history.push('/error')
                        }
                    </div>

            }

            {
                playerStatus ?
                    <MyReactPlayer url={videoURL}/>
                    :
                    null
            }

        </div>
    );
};

export default CourseIdPage;