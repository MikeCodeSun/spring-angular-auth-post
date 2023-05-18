package com.example.server.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.server.detail.CustomUserDetail;
import com.example.server.model.User;
import com.example.server.repository.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {

  @Autowired 
  private UserRepository userRepository;

  public  User findUserbyId(Integer id) {
    Optional<User>  optUser = userRepository.findById(id);
    User user = optUser.orElse(null);
    return user;
  }

  public boolean existUserByUsername(String username) {
    User user = userRepository.finUserByUsername(username);
    if (user == null) {
      return false;
    }
    return true;
  }

  @Override
  public CustomUserDetail loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.finUserByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("username not find");
    }
    return new CustomUserDetail(user);
  }
}
