package com.example.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.server.model.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {
  
}
