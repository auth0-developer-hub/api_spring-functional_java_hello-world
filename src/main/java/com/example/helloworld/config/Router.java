package com.example.helloworld.config;

import static org.springframework.web.servlet.function.RouterFunctions.route;

import com.example.helloworld.handlers.MessageHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class Router {

  private static final String API = "/api";

  private static final String MESSAGES = "/messages";

  private static final String PUBLIC = "/public";

  private static final String PROTECTED = "/protected";

  private static final String ADMIN = "/admin";

  public static final String PROTECTED_PATTERN = API.concat(MESSAGES).concat(PROTECTED);

  public static final String ADMIN_PATTERN = API.concat(MESSAGES).concat(ADMIN);

  @Bean
  public RouterFunction<ServerResponse> apiRouter(
    final MessageHandler messageHandler,
    final ErrorHandler errorHandler
  ) {
    return route()
      .path(API, () ->
        route()
          .path(MESSAGES, () ->
            route()
              .GET(PUBLIC, messageHandler::publicMessage)
              .GET(PROTECTED, messageHandler::protectedMessage)
              .GET(ADMIN, messageHandler::adminMessage)
              .build()
          )
          .build()
      )
      .onError(AccessDeniedException.class, errorHandler::handleAccessDenied)
      .onError(Throwable.class, errorHandler::handleInternalError)
      .build();
  }
}
