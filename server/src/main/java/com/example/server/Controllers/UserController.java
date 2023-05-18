package com.example.server.Controllers;

import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.server.detail.CustomUserDetail;
import com.example.server.model.Follower;
import com.example.server.model.User;
import com.example.server.repository.UserRepository;
import com.example.server.service.FollowerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("user")
public class UserController {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private FollowerService followerService;

  private User getCurrentUser() {
    CustomUserDetail customUserDetail = (CustomUserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String username = customUserDetail.getUsername();
    User user = userRepository.finUserByUsername(username);

    return user;
  }
  
  @PostMapping("register")
  public ResponseEntity<?> register(@Valid @RequestBody User user, BindingResult bResult) {
    if(bResult.hasErrors()) {
      Map<String, String> errors = new HashMap<>();
      for( FieldError err: bResult.getFieldErrors()){
        errors.put(err.getField(), err.getDefaultMessage());
      }
      return new ResponseEntity<>(errors, HttpStatus.OK);
    }
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    String encodePassword = bCryptPasswordEncoder.encode(user.getPassword());
    user.setPassword(encodePassword);
    userRepository.save(user);
    Map<String, String> message = new HashMap<>();
    message.put("message", "register successfully");
    return new ResponseEntity<>(message, HttpStatus.OK);
  }

  @GetMapping("u/{id}")
  public ResponseEntity<?> getUser(@PathVariable("id") Integer id){
    User currentUser =getCurrentUser();
    
    Optional<User> optUser = userRepository.findById(id);
    
    if(optUser.isEmpty()) {
      return new ResponseEntity<String>("Not find user id: " + id, HttpStatus.BAD_REQUEST);
    }
    User user = optUser.get();
    Follower follower = followerService.hasFollowBetweenTwoUsers(currentUser, user);
    if(follower == null) {
      user.setIsFollow(false);
    }else {
      user.setIsFollow(true);
    }
    user.setSizeOfFollowers();
    user.setSizeOfFollowing();
    return new ResponseEntity<User>(user, HttpStatus.OK);
  }
  
  @GetMapping("login")
  public String login() {
    return "login";
  }
  
  @GetMapping("hello")
  public String hello() {
    User  user = getCurrentUser();
    
    return user.getUsername();
  }
  @GetMapping("bye")
  public String bye() {
    return "bye";
  }
  @GetMapping("home")
  public String home() {
    return "Post";
  }

  @PostMapping("upload")
  public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile multipartFile) {
    User user = getCurrentUser();
    if(user == null) {
      return new ResponseEntity<>("no user", null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    String originalName = multipartFile.getOriginalFilename();
    String contentType = multipartFile.getContentType();
    Long size = multipartFile.getSize();
  
    if(size > 1100000){
      return new ResponseEntity<>("Image file too big!", null, HttpStatus.BAD_REQUEST);
    }
    if(contentType != null && !contentType.equals("image/jpeg") && !contentType.equals("image/jpg") && !contentType.equals("image/png")){
      return new ResponseEntity<>("Not support Image file type!", null, HttpStatus.BAD_REQUEST);
    }

    String newName = UUID.randomUUID().toString();
    String extName =  "";
    Integer index  = 0;
    if(originalName != null) {
      index = originalName.lastIndexOf('.');
      extName = originalName.substring(index+1);
    }
    String newImageName = newName + "." + extName;
    // if no folder create folder
    Path dirPath = Paths.get("image");
    if(Files.notExists(dirPath)){
      try {
        Files.createDirectory(dirPath);
      } catch (Exception e) {
        return new ResponseEntity<>("Exception" + e, null, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    // if user already have image delete old one
    
      String oldImage = user.getImage();
      if(oldImage != null) {
        Path oldImagePath = dirPath.resolve(oldImage);
        try {
          Files.deleteIfExists(oldImagePath);
        } catch (Exception e) {
          return new ResponseEntity<>("Exception" + e, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
      }
    
    // save image
    Path newImagePath = dirPath.resolve(newImageName);
    try {
      Files.copy(multipartFile.getInputStream(), newImagePath, StandardCopyOption.REPLACE_EXISTING);
    } catch (Exception e) {
      return new ResponseEntity<>("Exception" + e, null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    // save image in db
    user.setImage(newImageName);
    userRepository.save(user);

    return new ResponseEntity<>(newImageName, HttpStatus.OK);
  }
}
