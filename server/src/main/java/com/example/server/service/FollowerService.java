package com.example.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.server.model.Follower;
import com.example.server.model.User;
import com.example.server.repository.FollowerRepository;

@Service
public class FollowerService {
  @Autowired
  private FollowerRepository followerRepository;

  public Follower hasFollowBetweenTwoUsers(User currentUser, User followUser) {
    Follower follower = followerRepository.hasFollow(currentUser, followUser);
    return follower;
  }

  public void followUser(User currentUser, User followUser) {
    Follower follower = new Follower();
    follower.setFrom(currentUser);
    follower.setTo(followUser);
    followerRepository.save(follower);
    return;
  }

  public void unfollowUser(Follower follower) {
    followerRepository.delete(follower);
    return;
  }
}
