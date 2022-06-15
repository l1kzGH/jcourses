export const initialAuthState = {isAuth: false};

const isAuthReducer = (state, action) => {
    switch (action.type) {
        case "setAuth":
            return {isAuth: action.payload}
        default:
            return state;
    }
}

export default isAuthReducer;