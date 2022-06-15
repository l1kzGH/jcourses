import React from 'react';
import "./myrate.css";

const MyRate = ({rate, rateCourse, subscribeInfo}) => {
    return (
        <div>
            <div className="stars">
                <input checked={subscribeInfo.rate === 1} type="radio" id="star1" name="rating" value="1" onClick={() => rateCourse(1)}/>
                <input checked={subscribeInfo.rate === 2} type="radio" id="star2" name="rating" value="2" onClick={() => rateCourse(2)}/>
                <input checked={subscribeInfo.rate === 3} type="radio" id="star3" name="rating" value="3" onClick={() => rateCourse(3)}/>
                <input checked={subscribeInfo.rate === 4} type="radio" id="star4" name="rating" value="4" onClick={() => rateCourse(4)}/>
                <input checked={subscribeInfo.rate === 5} type="radio" id="star5" name="rating" value="5" onClick={() => rateCourse(5)}/>

                <label htmlFor="star1" aria-label="Banana">1 star</label>
                <label htmlFor="star2">2 stars</label>
                <label htmlFor="star3">3 stars</label>
                <label htmlFor="star4">4 stars</label>
                <label htmlFor="star5">5 stars</label>
            </div>
        </div>
    );
};

export default MyRate;