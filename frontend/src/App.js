import React, {useEffect, useLayoutEffect, useReducer, useState} from "react";
import {BrowserRouter, Route} from "react-router-dom";
import AppRouter from "./components/AppRouter";
import NavBar from "./components/NavBar";
import "./App.css";
import isAuthReducer, {initialAuthState} from "./reducers/isAuthReducer";
import {AuthContext} from "./context/authContext";
import NotFoundPage from "./pages/notfound/NotFoundPage";

function App() {

    const [state, dispatch] = useReducer(isAuthReducer, initialAuthState);
    const [role, setRole] = useState();
    const [username, setUsername] = useState();
    const [id, setId] = useState();

    useEffect(() => {
        if (localStorage.getItem('auth')) {
            //console.log(typeof localStorage.getItem('auth'));
            dispatch({type: "setAuth", payload: true});
            //console.log(localStorage.getItem('role'));
            setRole(localStorage.getItem('role'));
            setUsername(localStorage.getItem('username'));
        }

    }, [])

    return (
        <AuthContext.Provider value={{
            isAuth: state.isAuth,
            setIsAuth: dispatch,

            role, setRole,
            username, setUsername
        }}>
            <BrowserRouter>
                <NavBar/>
                <AppRouter/>
            </BrowserRouter>
        </AuthContext.Provider>

    );
}

export default App;
