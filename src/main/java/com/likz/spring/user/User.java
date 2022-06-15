package com.likz.spring.user;

import com.likz.spring.course.Course;
import com.likz.spring.course.notification.Notification;
import com.likz.spring.follow.Follow;
import com.likz.spring.subscribe.Subscribe;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "isActive")
    private boolean isActive = false;
    @Column(name = "isLocked")
    private boolean isLocked = false;
    @Column(name = "imageUrl", nullable = false)
    private String image_url;
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Course> courses = new ArrayList<>();
    @OneToMany(mappedBy = "followAuthor", fetch = FetchType.LAZY)
    private List<Follow> authorFollows = new ArrayList<>();
    @OneToMany(mappedBy = "followUser", fetch = FetchType.LAZY)
    private List<Follow> userFollows = new ArrayList<>();
    @OneToMany(mappedBy = "subUser", fetch = FetchType.LAZY)
    private List<Subscribe> subscribes = new ArrayList<>();
    @OneToMany(mappedBy = "userNotify", fetch = FetchType.LAZY)
    private List<Notification> notifications = new ArrayList<>();

    public User(String name, String username, String password, String email, UserRole role) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.image_url = "default.png";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
        return Collections.singletonList(authority);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}