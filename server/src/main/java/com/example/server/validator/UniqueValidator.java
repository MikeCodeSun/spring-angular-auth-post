package com.example.server.validator;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.server.service.CustomUserDetailService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueValidator implements ConstraintValidator<Unique, String> {

  @Autowired
  private CustomUserDetailService customUserDetailService;
  
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if(customUserDetailService != null && customUserDetailService.existUserByUsername(value)){
      return  false;
    }
    return true;
  }
}
