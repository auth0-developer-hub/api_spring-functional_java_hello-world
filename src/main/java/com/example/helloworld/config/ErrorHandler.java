package com.example.helloworld.config;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.helloworld.models.APIMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import lombok.Value;

@Value
@RestControllerAdvice
public class ErrorHandler {

  private ObjectMapper mapper;

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NoHandlerFoundException.class)
  public APIMessage handleNotFound(final HttpServletRequest request, final Exception ex) {
    return APIMessage.from("Not found.");
  }

  /**
   * @param request required by the router handler
   */
  public ServerResponse handleInternalError(final Throwable error, final ServerRequest request) {
    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(APIMessage.from(error.getMessage()));
  }

  /**
   * @param request required by the authentication entry point
   */
  public void handleAuthenticationError(
    final HttpServletRequest request,
    final HttpServletResponse response,
    final AuthenticationException error
  ) throws IOException {
    final var errorMessage = "Unauthorized. %s".formatted(error.getMessage());
    final var message = APIMessage.from(errorMessage);
    final var json = mapper.writeValueAsString(message);

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write(json);
    response.flushBuffer();
  }
}
