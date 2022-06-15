package com.likz.spring.subscribe;

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
@Table(name = "subscribe")
public class Subscribe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User subUser;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course subCourse;
    @Column(name = "rate", nullable = false)
    private Long rate;

    public Subscribe(User subUser, Course subCourse) {
        this.subUser = subUser;
        this.subCourse = subCourse;
        this.rate = 0L;
    }
}
