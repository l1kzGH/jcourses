package com.likz.spring.follow;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class FollowDTO {

    private final String user_username;
    private final String author_username;

}
