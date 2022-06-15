package com.likz.spring.course;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class CreateCourseDTO {

    private final String author;
    private final String name;
    private final List<String> category_name;
    private final int price;

    private final String description;

}