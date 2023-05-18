package com.example.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import com.example.server.model.User;


public interface UserRepository extends JpaRepository<User, Integer> {
  
  @Query("SELECT u FROM User u WHERE u.username = ?1")
  User finUserByUsername(String username);
}
