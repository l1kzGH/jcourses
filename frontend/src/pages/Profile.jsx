import React, {useContext, useEffect, useState} from 'react';
import axios from "axios";
import '../styles/profile.css';
import {AuthContext} from "../context/authContext";
import EditForm from "../components/profile/EditForm";
import UserForm from "../components/profile/UserForm";

const Profile = () => {

    const {role} = useContext(AuthContext);
    const [userInfo, setUserInfo] = useState({});
    const [status, setStatus] = useState(true);

    useEffect(() => {
        axios({
            method: "get",
            url: "http://localhost:8080/user",
            withCredentials: true
        }).then((response => {
            setUserInfo({
                id: response.data.id,
                name: response.data.name,
                username: response.data.username,
                role: role,
                image_url: response.data.image_url
            })
        }));
    }, []);

    return (
        <div>
            <div className="container">
                <div className="row">
                    <div className="col-md-5 col-md-offset-3 col-sm-6 col-sm-offset-3">
                        <div className="card">
                            {
                                status ?
                                    <UserForm userInfo={userInfo} setStatus={setStatus}/>
                                    :
                                    <EditForm userInfo={userInfo} setStatus={setStatus}/>
                            }
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};


export default Profile;