package com.example.server.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// mvc 0518
@Configuration
public class MvcConfig implements WebMvcConfigurer  {
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    Path dirName = Paths.get("image");
    Path dirPath =  dirName.toAbsolutePath();
   
    registry.addResourceHandler("/" + dirName + "/**").addResourceLocations("file:///" + dirPath + "/");
  }
}
