package com.likz.spring.course;

import com.likz.spring.course.category.Category;
import com.likz.spring.course.category.CategoryService;
import com.likz.spring.course.notification.NotificationService;
import com.likz.spring.subscribe.Subscribe;
import com.likz.spring.subscribe.SubscribeDTO;
import com.likz.spring.subscribe.SubscribeService;
import com.likz.spring.user.User;
import com.likz.spring.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.*;

@Service
//@AllArgsConstructor
public class CourseService {

    @Value("${upload.path}")
    private String uploadPath;

    private final CourseRepo courseRepo;
    private final UserService userService;
    private final CategoryService categoryService;
    private final NotificationService notificationService;
    @Autowired
    private SubscribeService subscribeService;

    public CourseService(CourseRepo courseRepo, UserService userService, CategoryService categoryService, NotificationService notificationService) {
        this.courseRepo = courseRepo;
        this.userService = userService;
        this.categoryService = categoryService;
        this.notificationService = notificationService;
    }

    public List<CoursesResponse> findAll() {
        List<Course> courses = courseRepo.findAll();
        List<CoursesResponse> coursesResp = new ArrayList<>();

        for (Course c : courses) {
            coursesResp.add(new CoursesResponse(
                    c.getId(),
                    c.getAuthor().getUsername(),
                    c.getName(),
                    c.getCategories().stream().map(Category::getName).toList(),
                    c.getRating(),
                    c.getNumber_votes(),
                    c.getPrice(),
                    c.getImage_url(),
                    c.is_visible()
            ));
        }

        return coursesResp;
    }

    public Course findCourseById(Long id) {
        Course course = courseRepo.getById(id);
        return course;
    }

    public CourseDTO findById(Long id) {
        Course course = courseRepo.getById(id);

        CourseDTO courseDTO = new CourseDTO(
                course.getId(),
                course.getAuthor().getUsername(),
                course.getName(),
                course.getCategories().stream().map(Category::getName).toList(),
                course.getRating(),
                course.getNumber_votes(),
                course.getPrice(),
                course.getImage_url(),

                course.getDescription(),
                course.getVideo_count(),
                course.getRelease_date(),
                course.getUpdate_date(),
                course.is_visible()
        );

        return courseDTO;
    }

    public Course findByName(String name) {
        return courseRepo.findByName(name).orElse(null);
    }

    public ResponseEntity<String> create(CreateCourseDTO createCourseDTO) {

        User user = userService.getUserByUsername(createCourseDTO.getAuthor());
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < createCourseDTO.getCategory_name().size(); i++) {
            categories.add(categoryService.findByName(createCourseDTO.getCategory_name().get(i)));
            if (categories.get(i) == null) categories.remove(i);
        }
        if (categories.isEmpty())
            return ResponseEntity.status(404).body("No such categories found");

        Locale local = new Locale("ru", "RU");
        DateFormat df = DateFormat.getDateInstance(DateFormat.DATE_FIELD, local);

        Course course = new Course(
                user,
                createCourseDTO.getName(),
                categories,
                createCourseDTO.getPrice(),
                createCourseDTO.getDescription(),
                df.format(new Date())
        );

        courseRepo.save(course);

        return ResponseEntity.ok("Course created");
    }

    public ResponseEntity<?> delete(Course course) {
        deleteLinks(course.getId());
        courseRepo.delete(course);

        return ResponseEntity.ok("Course deleted");
    }

    @Query("DELETE FROM course_category WHERE (`course_id` = :course_id)")
    public ResponseEntity<?> deleteLinks(@Param("course_id") Long course_id) {
        return ResponseEntity.ok("Links deleted");
    }

    public List<CoursesResponse> findAllWithParams(int count, int page, String sortName, String search, String author,
                                                   String category, String visibility) {
        List<Course> allCourses = courseRepo.findAll();
        List<Course> courses = new ArrayList<>(count);

        if (visibility != null) {
            for (int i = 0; i < allCourses.size(); ) {
                if (!String.valueOf(allCourses.get(i).is_visible()).equals(visibility)) {
                    allCourses.remove(allCourses.get(i));
                } else i++;
            }
        }

        if (author != null) {
            for (int i = 0; i < allCourses.size(); ) {
                if (!allCourses.get(i).getAuthor().getUsername().equals(author)) {
                    allCourses.remove(allCourses.get(i));
                } else i++;
            }
        }

        if (search != null) {
            for (int i = 0; i < allCourses.size(); ) {
                if (!allCourses.get(i).getName().contains(search)) {
                    allCourses.remove(allCourses.get(i));
                } else i++;
            }
        }

        if (sortName != null) {
            switch (sortName) {
                case "name":
                    allCourses.sort(Comparator.comparing(Course::getName));
                    break;
                case "price":
                    allCourses.sort(Comparator.comparing(Course::getPrice));
                    break;
                case "rating":
                    allCourses.sort(Comparator.comparing(Course::getRating).reversed());
                    break;
                default:
                    ResponseEntity.status(404).body("Sorting error");
            }
        }

        for (int i = (count * page - count); i < (count * page); i++) {
            if (i < allCourses.size())
                courses.add(allCourses.get(i));
        }

        List<CoursesResponse> coursesResp = new ArrayList<>();

        for (Course c : courses) {
            coursesResp.add(new CoursesResponse(
                    c.getId(),
                    c.getAuthor().getUsername(),
                    c.getName(),
                    c.getCategories().stream().map(Category::getName).toList(),
                    c.getRating(),
                    c.getNumber_votes(),
                    c.getPrice(),
                    c.getImage_url(),

                    c.is_visible()
            ));
        }

        return coursesResp;
    }

    public Long getCountOfPages(Long count, String search, String visibility) {
        List<Course> allCourses = courseRepo.findAll();

        if (visibility != null) {
            for (int i = 0; i < allCourses.size(); ) {
                if (!String.valueOf(allCourses.get(i).is_visible()).equals(visibility)) {
                    allCourses.remove(allCourses.get(i));
                } else i++;
            }
        }

        if (search != null) {
            for (int i = 0; i < allCourses.size(); ) {
                if (!allCourses.get(i).getName().contains(search)) {
                    allCourses.remove(allCourses.get(i));
                } else i++;
            }
        }

        double pages = (double) allCourses.size() / count;
        if (pages % 1 == 0) {
            return (long) (pages);
        } else {
            return (long) (pages + 1);
        }
    }

    public ResponseEntity<?> updatePhoto(Long id, MultipartFile file) throws IOException {

        Course course = courseRepo.getById(id);
        if (course == null || file == null) {
            return ResponseEntity.status(404).body("Error with adding file");
        }

        String fileName = UUID.randomUUID().toString() + "." + file.getContentType().split("/")[1];
        file.transferTo(new File(uploadPath + "/" + fileName));

        //удаление старой картинки
        if (!course.getImage_url().equals("default.png")) {
            File oldFile = new File(uploadPath + "/" + course.getImage_url());
            oldFile.delete();
        }

        course.setImage_url(fileName);
        courseRepo.save(course);

        return ResponseEntity.ok("Photo uploaded");
    }

    public ResponseEntity<?> update(Long id, Integer price, String description) {
        Course course = courseRepo.getById(id);
        if (course == null) {
            return ResponseEntity.status(404).body("Course not found");
        }

        if (price != null) {
            course.setPrice(price);
        }

        if (description != null) {
            course.setDescription(description);
        }

        Locale local = new Locale("ru", "RU");
        DateFormat df = DateFormat.getDateInstance(DateFormat.DATE_FIELD, local);
        course.setUpdate_date(df.format(new Date()));

        courseRepo.save(course);
        return ResponseEntity.ok("Course updated");
    }

    public ResponseEntity<?> rate(Long id, String username, Long rating) {
        User user = userService.getUserByUsername(username);
        Course course = courseRepo.getById(id);

        if (course == null || user == null) {
            ResponseEntity.status(404).body("Course/User not found");
        }

        Subscribe subscribe = subscribeService.findByIds(new SubscribeDTO(user.getUsername(), course.getName()));

        if (subscribe == null) {
            ResponseEntity.status(404).body("Subscribe not found");
        }

        if (subscribe.getRate() == 0) {
            course.setRating((course.getRating() * course.getNumber_votes() + rating) / (course.getNumber_votes() + 1));
            course.setNumber_votes(course.getNumber_votes() + 1);

            subscribeService.setRate(subscribe, rating);
            courseRepo.save(course);

            return ResponseEntity.ok("Course successfully rated");
        } else {
            course.setRating(((course.getRating() * course.getNumber_votes() - subscribe.getRate()) + rating) / course.getNumber_votes());

            subscribeService.setRate(subscribe, rating);
            courseRepo.save(course);

            return ResponseEntity.ok("Course successfully re-rated");
        }
    }

    public ResponseEntity<?> freeze(Long id) {

        Course course = courseRepo.getById(id);

        if (course == null) {
            return ResponseEntity.status(404).body("Course not found");
        }

        if (course.is_visible() == true) {
            course.set_visible(false);
            courseRepo.save(course);
            return ResponseEntity.ok("Course has been removed from publication");
        } else {
            return ResponseEntity.ok("Course not public");
        }
    }

    public ResponseEntity<?> release(Long id) {

        Course course = courseRepo.getById(id);

        if (course == null) {
            return ResponseEntity.status(404).body("Course not found");
        }

        if (course.is_visible() == false) {
            course.set_visible(true);
            courseRepo.save(course);
            //отправка уведомлений пользователям подписанных на автора
            notificationService.create(course);

            return ResponseEntity.ok("Course is published");
        } else {
            return ResponseEntity.ok("Course already public");
        }
    }

    public void subOneVideo(Long id){
        Course course = courseRepo.getById(id);
        course.setVideo_count(course.getVideo_count() - 1);
        courseRepo.save(course);
    }

}
