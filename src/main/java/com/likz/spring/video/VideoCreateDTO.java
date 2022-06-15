package com.likz.spring.video;

import com.likz.spring.course.Course;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class VideoCreateDTO {

    private final String name;
    private final String status;
    private final Long course_id;

}
