package com.likz.spring.course.notification;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "notification")
@AllArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('AUTHOR')")
    public ResponseEntity<List<NotificationResponse>> findByUserUsername(@RequestParam String username) {
        return ResponseEntity.ok(notificationService.findByUserUsername(username));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('AUTHOR')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return notificationService.delete(id);
    }

}
