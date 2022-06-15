package com.likz.spring.user;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserDTO {

    private final Long id;
    private final String name;
    private final String username;
    private final UserRole role;
    private final String image_url;

}