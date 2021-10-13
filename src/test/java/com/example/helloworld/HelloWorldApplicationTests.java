package com.example.helloworld;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import utils.annotations.IntegrationTest;

@IntegrationTest class HelloWorldApplicationTests {

  @Autowired
  private ApplicationContext appContext;

  @Nested class when_the_application_stars {
    @Test void the_context_is_loaded() {

      assertThat(appContext).isNotNull();
    }
  }
}
