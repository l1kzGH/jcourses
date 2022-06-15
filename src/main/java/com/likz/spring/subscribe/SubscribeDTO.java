package com.likz.spring.subscribe;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SubscribeDTO {

    private final String user_username;
    private final String course_name;

}
