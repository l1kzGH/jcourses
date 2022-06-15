package com.likz.spring.registration.token;

import com.likz.spring.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "confirmationToken")
public class ConfirmationToken {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "token", nullable = false)
    private String token;
    @Column(name = "createAt", nullable = false)
    private LocalDateTime createAt;
    @Column(name = "expireAt", nullable = false)
    private LocalDateTime expireAt;
    @Column(name = "confirmAt")
    private LocalDateTime confirmAt;
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    public ConfirmationToken(String token, LocalDateTime createAt, LocalDateTime expireAt, User user) {
        this.token = token;
        this.createAt = createAt;
        this.expireAt = expireAt;
        this.user = user;
    }
}
