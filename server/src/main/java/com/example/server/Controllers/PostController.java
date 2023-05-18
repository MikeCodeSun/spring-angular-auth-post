package com.example.server.Controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.detail.CustomUserDetail;
import com.example.server.model.Follower;
import com.example.server.model.Post;
import com.example.server.model.User;
import com.example.server.repository.UserRepository;
import com.example.server.service.PostService;


import jakarta.validation.Valid;

@RestController
@RequestMapping("post")
public class PostController {

  @Autowired
  private PostService postService;

  @Autowired
  private UserRepository userRepository;
  
  private User principalUser() {
    CustomUserDetail customUserDetail = (CustomUserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String username = customUserDetail.getUsername();
    User user = userRepository.finUserByUsername(username);
    return user;
  }
  
  @PostMapping("add")
  public ResponseEntity<?> add(@Valid @RequestBody Post post, BindingResult bResult) {

    if(bResult.hasErrors()) {
      Map<String, String> errors = new HashMap<>();
      for(FieldError err:bResult.getFieldErrors()){
        errors.put(err.getField(), err.getDefaultMessage());
      }
      return new ResponseEntity<Map<String, String>>(errors, HttpStatus.BAD_REQUEST);
    }

    User user = principalUser();
    Post savedPost = postService.addNewPost(user, post.getContent());

    return new ResponseEntity<>(savedPost, HttpStatus.OK);
  }

  @GetMapping("all")
  public List<Post> getAllPosts() {
    return postService.getAllPosts();
  }

  @GetMapping("id/{id}")
  public Post getOnePost(@PathVariable("id") Integer id) {
    Optional<Post> optPost = postService.getOnePost(id);
    if(optPost.isEmpty()) {
      return new Post();
    }
    return optPost.get();
  }

  @DeleteMapping("delete/{id}")
  public String deteleOnePost(@PathVariable("id") Integer id) {
    Optional<Post> optPost = postService.getOnePost(id);
    if(optPost.isEmpty()) {
      return "No post find!";
    }
    User user = principalUser();
    if(optPost.get().getUser().getId() != user.getId()) {
      return "Not your post";
    }
    postService.deletePost(id);
    return "delete";
  }

  @PutMapping("update/{id}")
  public ResponseEntity<?> updatePost(@PathVariable("id") Integer id, @Valid @RequestBody Post post, BindingResult bResult) {
    if(bResult.hasErrors()) {
      Map<String, String> errors = new HashMap<>();
      for(FieldError err:bResult.getFieldErrors()){
        errors.put(err.getField(), err.getDefaultMessage());
      }
      return new ResponseEntity<Map<String, String>>(errors, HttpStatus.BAD_REQUEST);
    }
    Optional<Post> optPost = postService.getOnePost(id);
    if(optPost.isEmpty()) {
      return new ResponseEntity<>("No post find!", HttpStatus.BAD_REQUEST);
    }

    User user = principalUser();
    if(optPost.get().getUser().getId() != user.getId()) {
      return new ResponseEntity<>("Not your post", HttpStatus.BAD_REQUEST);
    }

    Post updatedPost = postService.updatePost(optPost.get(), post.getContent());
    return new ResponseEntity<>(updatedPost, HttpStatus.OK);
  }

  // 0517 follow post
  @GetMapping("follow")
  public ResponseEntity<List<Post>> getFollowPost() {
    List<Post> posts = new ArrayList<>();
    User user = principalUser();
    

    List<Follower> followers =user.getUFollow();
    for(Follower f:followers) {
        for(Post p : f.getTo().getPosts()){
          posts.add(p);
        }
    }
    if(posts.size() > 0) {
      Collections.sort(posts, new Comparator<Post>(){
        @Override
        public int compare(Post o1, Post o2) {
          return o1.getId().compareTo(o2.getId());
        }
      });
    }
    return new ResponseEntity<>(posts, HttpStatus.OK);
  }

}
