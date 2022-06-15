package com.likz.spring.course;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "courses")
//AllArgsConstructor
public class CourseController {

    @Value("${upload.path_3}")
    private String uploadPath;

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        CourseDTO courseDTO = courseService.findById(id);
        if (courseDTO == null) return ResponseEntity.status(404).body("Course not found");

        return ResponseEntity.ok(courseService.findById(id));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('AUTHOR')")
    public ResponseEntity<?> create(@RequestBody CreateCourseDTO createCourseDTO) {
        Course course = courseService.findByName(createCourseDTO.getName());
        if (course != null) return ResponseEntity.status(404).body("Such course already exists");

        return courseService.create(createCourseDTO);
    }

    @DeleteMapping("{name}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('AUTHOR')")
    public ResponseEntity<?> delete(@PathVariable String name) {

        Course course = courseService.findByName(name);
        if (course == null)
            return ResponseEntity.status(404).body("Course not found");
        else {
            File file = new File(uploadPath + "/" + course.getId());
            if(file.exists())
                file.delete();

            return courseService.delete(course);
        }
    }

    @GetMapping("/count/{count}")
    public ResponseEntity<?> getCountOfPages(@PathVariable Long count,
                                             @RequestParam(required = false) String search,
                                             @RequestParam(required = false) String visibility) {
        return ResponseEntity.ok(courseService.getCountOfPages(count, search, visibility));
    }

    @PatchMapping("{id}/photo")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('AUTHOR')")
    public ResponseEntity<?> updatePhoto(@PathVariable Long id,
                                         @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(courseService.updatePhoto(id, file));
    }

    @GetMapping()
    public ResponseEntity<List<CoursesResponse>> findAllWithParams(
            @RequestParam int count,
            @RequestParam int page,
            @RequestParam(required = false) String sortName,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String visibility) {
        return ResponseEntity.ok(courseService.findAllWithParams(count, page, sortName, search, author, category, visibility));
    }

    @PatchMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('AUTHOR')")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestParam(required = false) Integer price,
                                    @RequestParam(required = false) String description){
        return ResponseEntity.ok(courseService.update(id, price, description));
    }

    @PatchMapping("{id}/rate")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('AUTHOR')")
    public ResponseEntity<?> rate(@PathVariable Long id,
                                    @RequestParam String username,
                                    @RequestParam Long rating){
        return ResponseEntity.ok(courseService.rate(id, username, rating));
    }

    @PatchMapping("{id}/freeze")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('AUTHOR')")
    public ResponseEntity<?> freeze(@PathVariable Long id){
        return ResponseEntity.ok(courseService.freeze(id));
    }

    @PatchMapping("{id}/release")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('AUTHOR')")
    public ResponseEntity<?> release(@PathVariable Long id){
        return ResponseEntity.ok(courseService.release(id));
    }

}
