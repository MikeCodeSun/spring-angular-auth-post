package com.example.server.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.detail.CustomUserDetail;
import com.example.server.model.Follower;
import com.example.server.model.User;
import com.example.server.service.CustomUserDetailService;
import com.example.server.service.FollowerService;


@RestController
@RequestMapping("follow")
public class FollowerController {

  @Autowired
  private CustomUserDetailService customUserDetailService;
  @Autowired
  private FollowerService followerService;

  public User getCurrentUser() {
    CustomUserDetail customUserDetail = (CustomUserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = customUserDetail.getUser();
    return user;
  }
  
  @PostMapping("user/{id}")
  public ResponseEntity<?> followOrUnfollowUser(@PathVariable("id") Integer id){
    System.out.println(id);
    User followUser = customUserDetailService.findUserbyId(id);
    if(followUser == null){
      return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }
    User currentUser = getCurrentUser();

    Follower follower = followerService.hasFollowBetweenTwoUsers(currentUser, followUser);

    if(follower == null ) {
      followerService.followUser(currentUser, followUser);
      return new ResponseEntity<>("follow", HttpStatus.OK);
    } else {
      followerService.unfollowUser(follower);
      return new ResponseEntity<>("unollow", HttpStatus.OK);
    }
  }

  
}
