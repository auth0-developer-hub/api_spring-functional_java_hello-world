package com.example.helloworld.config;

import javax.servlet.http.HttpServletRequest;

import com.example.helloworld.models.ErrorMessage;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@RestControllerAdvice
public class GlobalErrorHandler {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NoHandlerFoundException.class)
  public ErrorMessage handleNotFound(final HttpServletRequest request, final Exception ex) {
    return ErrorMessage.from("Not Found");
  }

  /**
   * @param request required by the router handler
   */
  public ServerResponse handleInternalError(final Throwable error, final ServerRequest request) {
    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(ErrorMessage.from(error.getMessage()));
  }
}
