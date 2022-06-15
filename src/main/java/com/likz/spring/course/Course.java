package com.likz.spring.course;

import com.likz.spring.course.category.Category;
import com.likz.spring.course.notification.Notification;
import com.likz.spring.subscribe.Subscribe;
import com.likz.spring.user.User;
import com.likz.spring.video.Video;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "author", referencedColumnName = "id")
    private User author;
    @Column(name = "name", nullable = false)
    private String name;
    @ManyToMany
    @JoinTable(name="course_category",
            joinColumns=@JoinColumn(name="course_id", referencedColumnName = "id"),
            inverseJoinColumns=@JoinColumn(name="category_id", referencedColumnName = "id"))
    private List<Category> categories;
    @Column(name = "rating", nullable = false)
    private double rating;
    @Column(name = "numberVotes", nullable = false)
    private Long number_votes;
    @Column(name = "price", nullable = false)
    private int price;
    @Column(name = "imageUrl", nullable = false)
    private String image_url;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "videoCount", nullable = false)
    private Long video_count;
    @Column(name = "releaseDate", nullable = false)
    private String release_date;
    @Column(name = "updateDate", nullable = false)
    private String update_date;
    @Column(name = "isVisible", nullable = false)
    private boolean is_visible;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<Video> videos = new ArrayList<>();
    @OneToMany(mappedBy = "subCourse", fetch = FetchType.LAZY)
    private List<Subscribe> subscribes = new ArrayList<>();
    @OneToMany(mappedBy = "courseNotify", fetch = FetchType.LAZY)
    private List<Notification> notifications = new ArrayList<>();

    public Course(User author, String name, List<Category> categories, int price, String description, String release_date) {
        this.author = author;
        this.name = name;
        this.categories = categories;
        this.price = price;
        this.description = description;
        this.release_date = release_date;

        this.rating = 0;
        this.number_votes = 0L;
        this.video_count = 0L;
        this.update_date = release_date;
        this.image_url = "default.png";
    }
}
