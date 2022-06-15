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
public class CourseDTO {

    private final Long id;
    private final String author;
    private final String name;
    private final List<String> category_name;
    private final double rating;
    private final Long number_votes;
    private final int price;
    private final String image_url;

    private final String description;
    private final Long video_count;
    private final String release_date;
    private final String update_date;
    private final boolean is_visible;

}
