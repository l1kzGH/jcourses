package com.likz.spring.subscribe;

import com.likz.spring.course.Course;
import com.likz.spring.course.CourseService;
import com.likz.spring.user.User;
import com.likz.spring.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SubscribeService {

    private final SubscribeRepo subscribeRepo;
    private final UserService userService;
    private final CourseService courseService;

    public Subscribe findByIds(SubscribeDTO subscribeDTO) {
        User user = userService.getUserByUsername(subscribeDTO.getUser_username());
        Course course = courseService.findByName(subscribeDTO.getCourse_name());

        if (user == null || course == null) {
            return null;
        }

        Subscribe subscribe = subscribeRepo.findByIds(user.getId(), course.getId());

        if (subscribe == null) {
            return null;
        }

        return subscribe;
    }

    public ResponseEntity<?> create(SubscribeDTO subscribeDTO) {

        Subscribe subscribe = findByIds(subscribeDTO);
        if (subscribe != null) {
            return ResponseEntity.status(404).body("Subscribe already exists");
        }

        User user = userService.getUserByUsername(subscribeDTO.getUser_username());
        Course course = courseService.findByName(subscribeDTO.getCourse_name());

        if (user == null || course == null) {
            return ResponseEntity.status(404).body("User/Course not found");
        }

        Subscribe newSubscribe = new Subscribe(
                user,
                course
        );

        subscribeRepo.save(newSubscribe);

        return ResponseEntity.ok("You have subscribed to the course");
    }

    public ResponseEntity<?> delete(SubscribeDTO subscribeDTO) {
        User user = userService.getUserByUsername(subscribeDTO.getUser_username());
        Course course = courseService.findByName(subscribeDTO.getCourse_name());

        subscribeRepo.delete(user.getId(), course.getId());

        return ResponseEntity.ok("You have unsubscribed from the course");
    }

    public ResponseEntity<?> checkSubscribeByIds(String user_username, String course_name) {

        Subscribe subscribe = findByIds(new SubscribeDTO(user_username, course_name));

        if (subscribe == null) {
            return ResponseEntity.ok(null);
        }

        SubscribeResponse subscribeResponse = new SubscribeResponse(
                subscribe.getSubUser().getUsername(),
                subscribe.getSubCourse().getName(),
                subscribe.getId(),
                subscribe.getRate()
        );

        return ResponseEntity.ok(subscribeResponse);
    }

    public List<SubscribeResponse> findAllSubscribes(Long id) {

        List<Subscribe> subscribes = subscribeRepo.findByUserId(id);
        List<SubscribeResponse> subscribeResponses = new ArrayList<>();

        for (Subscribe s : subscribes) {
            Course course = courseService.findByName(s.getSubCourse().getName());

            subscribeResponses.add(new SubscribeResponse(
                    s.getSubUser().getUsername(),
                    s.getSubCourse().getName(),
                    course.getId(),
                    s.getRate()
            ));
        }

        return subscribeResponses;
    }

    public void setRate(Subscribe subscribe, Long rating){
        subscribe.setRate(rating);
        subscribeRepo.save(subscribe);
    }

}
