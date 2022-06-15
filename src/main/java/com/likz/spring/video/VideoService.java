package com.likz.spring.video;

import com.likz.spring.course.Course;
import com.likz.spring.course.CourseService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.*;

@Service
//@AllArgsConstructor
public class VideoService {

    @Value("${upload.path_3}")
    private String uploadPath;

    private final VideoRepo videoRepo;
    private final CourseService courseService;

    public VideoService(VideoRepo videoRepo, CourseService courseService) {
        this.videoRepo = videoRepo;
        this.courseService = courseService;
    }

    public Video findById(Long id) {
        Video video = videoRepo.getById(id);
        return video;
    }

    public List<VideoResponse> findByCourseId(Long id, String status) {
        List<Video> videos = videoRepo.findAllByCourseId(id);
        List<VideoResponse> videoResponses = new ArrayList<>();

        if(status != null){
            for(int i=0; i<videos.size(); ){
                if(!videos.get(i).getStatus().equals(status)){
                    videos.remove(i);
                } else i++;
            }
        }

        for (Video video : videos) {
            videoResponses.add(new VideoResponse(
                    video.getId(),
                    video.getName(),
                    video.getStatus(),
                    video.getVideo_url(),
                    video.getCourse().getId()
            ));
        }

        return videoResponses;
    }

    public ResponseEntity<?> create(VideoCreateDTO videoDTO) throws IOException {

        Course course = courseService.findCourseById(videoDTO.getCourse_id());
        course.setVideo_count(Integer.toUnsignedLong(course.getVideos().size()) + 1);

        if (course == null)
            ResponseEntity.status(404).body("Course not found");

        Video video = new Video(
                videoDTO.getName(),
                videoDTO.getStatus(),
                course
        );

        videoRepo.save(video);

        return ResponseEntity.ok(video.getId());
    }

    public ResponseEntity<?> updateVideo(Long id, MultipartFile file) throws IOException {

        Video video = videoRepo.getById(id);
        if (video == null || file == null) {
            return ResponseEntity.status(404).body("Error with adding file");
        }

        //при необходимости создает папку с id курса (для видео)
        File courseDir = new File(uploadPath + "/" + video.getCourse().getId());
        if (!courseDir.exists()) {
            courseDir.mkdirs();
        }

        String fileName = UUID.randomUUID().toString() + "." + file.getContentType().split("/")[1];
        file.transferTo(new File(courseDir + "/" + fileName));

        video.setVideo_url(fileName);

        Locale local = new Locale("ru", "RU");
        DateFormat df = DateFormat.getDateInstance(DateFormat.DATE_FIELD, local);
        video.getCourse().setUpdate_date(df.format(new Date()));

        videoRepo.save(video);

        return ResponseEntity.ok("Video uploaded");
    }

    public ResponseEntity<?> deleteVideo(Long id, Long video_id){
        Video video = videoRepo.getById(video_id);
        Course course = courseService.findCourseById(id);

        if (video == null || course == null) {
            return ResponseEntity.status(404).body("Video/Course not found");
        }

        File file = new File(uploadPath + "/" + video.getCourse().getId() + "/" + video.getVideo_url());
        file.delete();

        courseService.subOneVideo(id);
        videoRepo.delete(video_id);

        return ResponseEntity.ok("Video deleted");
    }

}
