package com.likz.spring.follow;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "follow")
@AllArgsConstructor
public class FollowController {

    private final FollowService followService;

    @GetMapping()
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('AUTHOR')")
    public ResponseEntity<?> findFollow(@RequestParam String user_username,
                                        @RequestParam String author_username) {
        return followService.checkFollowByIds(user_username, author_username);
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('AUTHOR')")
    public ResponseEntity<?> create(@RequestBody FollowDTO followDTO) {
        return followService.create(followDTO);
    }

    @DeleteMapping()
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('AUTHOR')")
    public ResponseEntity<?> delete(@RequestBody FollowDTO followDTO) {
        return followService.delete(followDTO);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('AUTHOR')")
    public ResponseEntity<List<FollowDTO>> findAllFollows(@PathVariable Long id){
        return ResponseEntity.ok(followService.findAllFollowsByUser(id));
    }

}
