package com.example.helloworld.config;

import javax.servlet.http.HttpServletRequest;

import com.example.helloworld.models.APIMessage;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@RestControllerAdvice
public class ErrorHandler {

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
}
