package com.likz.spring.user;

import com.likz.spring.course.Course;
import com.likz.spring.registration.token.ConfirmationToken;
import com.likz.spring.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.NameNotFoundException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
//@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";
    @Value("${upload.path_2}")
    private String uploadPath;

    private final UserRepo userRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    public UserService(UserRepo userRepo, BCryptPasswordEncoder bCryptPasswordEncoder, ConfirmationTokenService confirmationTokenService) {
        this.userRepo = userRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.confirmationTokenService = confirmationTokenService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepo
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String registerUser(User user){
        //todo: если не подтвердил по почте
        //поиск совпадений по email
        boolean userEmailExist = userRepo
                .findByEmail(user.getEmail())
                .isPresent();
        if(userEmailExist){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Email already taken");
        }
        // поиск совпадений по username
        boolean userUsernameExist = userRepo
                .findByUsername(user.getUsername())
                .isPresent();
        if(userUsernameExist){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Username already taken");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepo.save(user);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

    public int enableUser(String email) {
        return userRepo.enableUser(email);
    }

    public UserDTO getCurrentDTOUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepo.findByUsername(auth.getName());
        UserDTO userDTO = new UserDTO(
                user.get().getId(),
                user.get().getName(),
                user.get().getUsername(),
                user.get().getRole(),
                user.get().getImage_url()
        );
        return userDTO;
    }

    public UserDTO getDTOUserById(Long id) {
        Optional<User> user = userRepo.findById(id);
        UserDTO userDTO = new UserDTO(
                user.get().getId(),
                user.get().getName(),
                user.get().getUsername(),
                user.get().getRole(),
                user.get().getImage_url()
        );
        return userDTO;
    }

    public User getUserById(Long id){
        Optional<User> user = userRepo.findById(id);
        return user.get();
    }

    public UserDTO getDTOUserByUsername(String username){
        Optional<User> user = userRepo.findByUsername(username);
        UserDTO userDTO = new UserDTO(
                user.get().getId(),
                user.get().getName(),
                user.get().getUsername(),
                user.get().getRole(),
                user.get().getImage_url()
        );
        return userDTO;
    }

    public User getUserByUsername(String username){
        Optional<User> user = userRepo.findByUsername(username);
        return user.orElse(null);
    }

    public ResponseEntity<?> updatePhoto(Long id, MultipartFile file) throws IOException {

        User user = userRepo.getById(id);
        if (user == null || file == null) {
            return ResponseEntity.status(404).body("Error with adding file");
        }

        String fileName = UUID.randomUUID().toString() + "." + file.getContentType().split("/")[1];
        file.transferTo(new File(uploadPath + "/" + fileName));

        //удаление старой картинки
        if(!user.getImage_url().equals("default.png")) {
            File oldFile = new File(uploadPath + "/" + user.getImage_url());
            oldFile.delete();
        }

        user.setImage_url(fileName);
        userRepo.save(user);

        return ResponseEntity.ok("Photo uploaded");
    }

    public ResponseEntity<?> update(Long id, String name, String username) {

        if(name == null && username == null)
            return ResponseEntity.status(404).body("Error with updating");

        User user = userRepo.findById(id).get();
        if(user == null)
            return ResponseEntity.status(404).body("User not found");

        if(name != null){
            user.setName(name);
        }

        if(username != null && userRepo.findByUsername(username).isPresent() == false){
            user.setUsername(username);
        }

        userRepo.save(user);
        return ResponseEntity.ok("User updated");
    }

    public ResponseEntity<?> deletePhoto(Long id){
        User user = userRepo.getById(id);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }
        if(user.getImage_url().equals("default.png")){
            return ResponseEntity.ok("Photo already in default");
        }

        File oldFile = new File(uploadPath + "/" + user.getImage_url());
        oldFile.delete();

        user.setImage_url("default.png");
        userRepo.save(user);

        return ResponseEntity.ok("Photo has been returned to default");
    }

}
