package com.likz.spring.course.notification;

import com.likz.spring.course.Course;
import com.likz.spring.follow.FollowDTO;
import com.likz.spring.follow.FollowService;
import com.likz.spring.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class NotificationService {

    private final NotificationRepo notificationRepo;
    private final FollowService followService;
    private final UserService userService;

    public ResponseEntity<?> create(Course course) {

        if (course != null) {
            List<FollowDTO> followDTOs = followService.findAllFollowsByAuthor(course.getAuthor().getId());

            for(FollowDTO f: followDTOs){
                Notification notification = new Notification(
                        course,
                        userService.getUserByUsername(f.getUser_username())
                );

                notificationRepo.save(notification);
            }

            return ResponseEntity.ok("Notifications created");
        } else
            return ResponseEntity.status(404).body("Course not found");
    }

    public List<NotificationResponse> findByUserUsername(String username) {

        List<Notification> notifications = notificationRepo.findAll();

        for (int i = 0; i < notifications.size(); ) {
            if (!notifications.get(i).getUserNotify().getUsername().equals(username)) {
                notifications.remove(i);
            } else
                i++;
        }

        List<NotificationResponse> notificationResponses = new ArrayList<>();

        for (Notification n : notifications) {
            notificationResponses.add(new NotificationResponse(
                    n.getId(),
                    n.getCourseNotify().getAuthor().getUsername(),
                    n.getCourseNotify().getId(),
                    n.getCourseNotify().getName(),
                    n.is_read()
            ));
        }

        return notificationResponses;
    }

    public ResponseEntity<?> delete(Long id){

        Notification notification = notificationRepo.getById(id);

        if(notification != null) {
            notificationRepo.delete(id);
            return ResponseEntity.ok("Notification was successfully deleted");
        } else
            return ResponseEntity.status(404).body("Notification not found");
    }

}
