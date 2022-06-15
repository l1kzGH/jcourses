import React from 'react';
import ReactPlayer from "react-player";

const MyReactPlayer = ({url}) => {
    return (
        <div>
            <ReactPlayer
                style={{margin: "auto", backgroundColor: "black"}}
                width="854px" height="480px" controls
                url={url}
                config={{
                    file: {
                        attributes: {
                            controlsList: 'nodownload',
                            onContextMenu: e => e.preventDefault()
                        }
                    }
                }}
            />
        </div>
    );
};

export default MyReactPlayer;