package com.example.helloworld.config;

import static org.springframework.web.servlet.function.RouterFunctions.route;

import com.example.helloworld.handlers.MessageHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class Router {

  @Bean
  public RouterFunction<ServerResponse> apiRouter(
    final MessageHandler messageHandler,
    final GlobalErrorHandler globalErrorHandler
  ) {
    return route()
      .path("/api", () ->
        route()
          .path("/messages", () ->
            route()
              .GET("/public", messageHandler::getPublic)
              .GET("/protected", messageHandler::getProtected)
              .GET("/admin", messageHandler::getAdmin)
              .build()
          )
          .build()
      )
      .onError(Throwable.class, globalErrorHandler::handleInternalError)
      .build();
  }
}
