package com.likz.spring.subscribe;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SubscribeResponse {

    private final String user_username;
    private final String course_name;

    private final Long course_id;
    private final Long rate;

}
