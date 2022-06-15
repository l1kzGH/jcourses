package com.likz.spring.video;

import com.likz.spring.course.Course;
import com.likz.spring.user.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "video")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "status", nullable = false)
    private String status;
    @Column(name = "videoUrl")
    private String video_url;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;

    public Video(String name, String status, Course course) {
        this.name = name;
        this.status = status;
        this.video_url = null;
        this.course = course;
    }
}
