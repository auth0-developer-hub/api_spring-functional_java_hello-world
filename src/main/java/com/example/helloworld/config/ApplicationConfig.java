package com.example.helloworld.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationConfig implements WebMvcConfigurer {

  @Autowired
  private ApplicationProperties applicationProps;

  @Override
  public void addCorsMappings(final CorsRegistry registry) {
    registry.addMapping("/api/**")
      .allowedOrigins(applicationProps.getClientOriginUrl());
  }
}
