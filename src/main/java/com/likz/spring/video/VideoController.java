package com.likz.spring.video;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "video")
@AllArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @GetMapping("{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Video video = videoService.findById(id);
        if (video == null)
            return ResponseEntity.status(404).body("Video not found");

        return ResponseEntity.ok(video);
    }

    @GetMapping("/course/{id}")
    public ResponseEntity<?> findByCourseId(@PathVariable Long id,
                                            @RequestParam(required = false) String status) {
        List<VideoResponse> videos = videoService.findByCourseId(id, status);
        if (videos == null)
            return ResponseEntity.status(404).body("Video not found");

        return ResponseEntity.ok(videos);
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('AUTHOR')")
    public ResponseEntity<?> create(@RequestBody VideoCreateDTO videoDTO) throws IOException {
        return videoService.create(videoDTO);
    }

    @PatchMapping("{id}/video")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('AUTHOR')")
    public ResponseEntity<?> updateVideo(@PathVariable Long id,
                                         @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(videoService.updateVideo(id, file));
    }

    @DeleteMapping("{id}/video")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('AUTHOR')")
    public ResponseEntity<?> deleteVideo(@PathVariable Long id,
                                         @RequestParam Long video_id) {
        return ResponseEntity.ok(videoService.deleteVideo(id, video_id));
    }

}
