package com.example.server.provider;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.server.detail.CustomUserDetail;
import com.example.server.service.CustomUserDetailService;

@Component
public class CustomAuthProvider implements AuthenticationProvider  {
  @Autowired
  private CustomUserDetailService customUserDetailService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    String password = (String)authentication.getCredentials();

    
    if(username.isBlank() || password.isBlank()) {
      throw new UsernameNotFoundException("Username and password must not be empty");
    }

    boolean isExist = customUserDetailService.existUserByUsername(username);

    if( isExist == false){
      throw new UsernameNotFoundException("Username not found");
    } else {
      BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
      CustomUserDetail customUserDetail = customUserDetailService.loadUserByUsername(username);
      boolean isValid = bc.matches(password, customUserDetail.getPassword());
      if (!isValid) {
        throw new AuthenticationCredentialsNotFoundException("Password not match");
      }else {
        return new UsernamePasswordAuthenticationToken(customUserDetail, password, null);
      }
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return true;
  }
}
