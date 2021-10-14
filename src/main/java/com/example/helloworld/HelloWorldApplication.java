package com.example.helloworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class HelloWorldApplication {

  public static void main(final String[] args) {
    SpringApplication.run(HelloWorldApplication.class, args);
  }
}
