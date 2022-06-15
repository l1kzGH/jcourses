package com.likz.spring.follow;

import com.likz.spring.user.User;
import com.likz.spring.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FollowService {

    private final FollowRepo followRepo;
    private final UserService userService;

    public Follow findByIds(FollowDTO followDTO) {
        User user = userService.getUserByUsername(followDTO.getUser_username());
        User author = userService.getUserByUsername(followDTO.getAuthor_username());

        if (user == null || author == null) {
            return null;
        }

        Follow follow = followRepo.findByIds(user.getId(), author.getId());

        if (follow == null) {
            return null;
        }

        return follow;
    }

    public ResponseEntity<?> checkFollowByIds(String author_username, String user_username) {

        Follow follow = findByIds(new FollowDTO(author_username, user_username));

        if (follow == null) {
            return ResponseEntity.ok("false");
        }

        return ResponseEntity.ok("true");
    }

    public ResponseEntity<?> create(FollowDTO followDTO) {

        if (findByIds(followDTO) != null) {
            return ResponseEntity.status(404).body("Follow already exists");
        }

        User user = userService.getUserByUsername(followDTO.getUser_username());
        User author = userService.getUserByUsername(followDTO.getAuthor_username());

        if (user == null || author == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        Follow follow = new Follow(
                user,
                author
        );

        followRepo.save(follow);

        return ResponseEntity.ok("You have subscribed to the author");
    }

    public ResponseEntity<?> delete(FollowDTO followDTO) {
        User user = userService.getUserByUsername(followDTO.getUser_username());
        User author = userService.getUserByUsername(followDTO.getAuthor_username());

        followRepo.delete(user.getId(), author.getId());

        return ResponseEntity.ok("You have unsubscribed from the author");
    }

    public List<FollowDTO> findAllFollowsByUser(Long id) {

        List<Follow> follows = followRepo.findByUserId(id);
        List<FollowDTO> followDTOs = new ArrayList<>();

        for (Follow f : follows) {
            followDTOs.add(new FollowDTO(
                    f.getFollowUser().getUsername(),
                    f.getFollowAuthor().getUsername()
            ));
        }

        return followDTOs;
    }

    public List<FollowDTO> findAllFollowsByAuthor(Long id) {

        List<Follow> follows = followRepo.findByAuthorId(id);
        List<FollowDTO> followDTOs = new ArrayList<>();

        for (Follow f : follows) {
            followDTOs.add(new FollowDTO(
                    f.getFollowUser().getUsername(),
                    f.getFollowAuthor().getUsername()
            ));
        }

        return followDTOs;
    }

}
