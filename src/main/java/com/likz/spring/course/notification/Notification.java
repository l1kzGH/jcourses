package com.likz.spring.course.notification;

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
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course courseNotify;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User userNotify;
    @Column(name = "isRead", nullable = false)
    private boolean is_read;

    public Notification(Course courseNotify, User userNotify) {
        this.courseNotify = courseNotify;
        this.userNotify = userNotify;
        this.is_read = false;
    }
}
