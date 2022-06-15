package com.likz.spring.subscribe;

import com.likz.spring.follow.FollowDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "subscribe")
@AllArgsConstructor
public class SubscribeController {

    public final SubscribeService subscribeService;

    @PostMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('AUTHOR')")
    public ResponseEntity<?> create(@RequestBody SubscribeDTO subscribeDTO) {
        return ResponseEntity.ok(subscribeService.create(subscribeDTO));
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('AUTHOR')")
    public ResponseEntity<?> delete(@RequestBody SubscribeDTO subscribeDTO) {
        return ResponseEntity.ok(subscribeService.delete(subscribeDTO));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('AUTHOR')")
    public ResponseEntity<?> findSubscribe(@RequestParam String user_username,
                                           @RequestParam String course_name) {
        return subscribeService.checkSubscribeByIds(user_username, course_name);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('AUTHOR')")
    public ResponseEntity<List<SubscribeResponse>> findAllSubscribes(@PathVariable Long id){
        return ResponseEntity.ok(subscribeService.findAllSubscribes(id));
    }

}
