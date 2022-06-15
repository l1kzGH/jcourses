package com.likz.spring.video;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class VideoResponse {

    private final Long id;
    private final String name;
    private final String status;
    private final String video_url;
    private final Long course_id;

}
