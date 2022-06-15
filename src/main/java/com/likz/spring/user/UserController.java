package com.likz.spring.user;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(path = "user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping()
    public ResponseEntity<UserDTO> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentDTOUser());
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getDTOUserById(id));
    }

    @GetMapping("author/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getDTOUserByUsername(username));
    }

    @PatchMapping("{id}/photo")
    public ResponseEntity<?> updatePhoto(@PathVariable Long id,
                                         @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(userService.updatePhoto(id, file));
    }

    @PatchMapping("{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestParam(required = false) String name,
                                    @RequestParam(required = false) String username){
        return ResponseEntity.ok(userService.update(id, name, username));
    }

    @GetMapping("{id}/photo/delete")
    public ResponseEntity<?> deletePhoto(@PathVariable Long id){
        return ResponseEntity.ok(userService.deletePhoto(id));
    }

}
