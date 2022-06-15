import React, {useContext} from 'react';
import {Button} from "react-bootstrap";
import {AuthContext} from "../../context/authContext";

const VideoItem = ({video, setVideoURL, setPlayerStatus, deleteVideo, courseInfo}) => {

    const {username} = useContext(AuthContext);

    const playVideo = () => {
        setVideoURL("http://localhost:3000/videos/" + video.course_id + "/" + video.video_url);
        setPlayerStatus(true);
        console.log(video.video_url);
    }

    return (
        <div className="videoItem">
            <div>
                <Button size="sm" variant="outline-success" onClick={playVideo}>Открыть</Button>
                {
                    username != null && courseInfo != null && username == courseInfo.author ?
                    <Button size="sm" variant="outline-danger" onClick={() => deleteVideo(video.id)}>Удалить</Button>
                    :
                    null
                }

            </div>
            <div>{video.name}</div>
            {
                video.status === "free" ?
                    <div>Бесплатный просмотр</div>
                    :
                    <div>Входит в подписку</div>
            }
        </div>
    );
};

export default VideoItem;