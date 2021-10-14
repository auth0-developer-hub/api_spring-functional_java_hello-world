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
    final ErrorHandler errorHandler
  ) {
    return route()
      .path("/api", () ->
        route()
          .path("/messages", () ->
            route()
              .GET("/public", messageHandler::publicMessage)
              .GET("/protected", messageHandler::protectedMessage)
              .GET("/admin", messageHandler::adminMessage)
              .build()
          )
          .build()
      )
      .onError(Throwable.class, errorHandler::handleInternalError)
      .build();
  }
}
