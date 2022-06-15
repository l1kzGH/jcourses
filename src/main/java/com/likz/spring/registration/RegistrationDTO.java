package com.likz.spring.registration;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationDTO {

    private final String name;
    private final String username;
    private final String password;
    private final String email;

}
