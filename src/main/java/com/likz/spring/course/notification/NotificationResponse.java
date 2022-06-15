package com.likz.spring.course.notification;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class NotificationResponse {

    private final Long id;
    private final String author_username;
    private final Long course_id;
    private final String course_name;
    private final boolean is_read;

}
