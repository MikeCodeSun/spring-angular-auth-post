package com.example.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.server.model.Follower;
import com.example.server.model.User;

public interface FollowerRepository extends JpaRepository<Follower, Integer> {
  
  @Query("SELECT f FROM Follower f WHERE (f.from = ?1 AND f.to = ?2)")
  Follower hasFollow(User currentUser,User followUser);
}
