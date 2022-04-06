package com.example.helloworld.handlers;

import static org.springframework.web.servlet.function.ServerResponse.ok;

import com.example.helloworld.config.security.CanReadMessages;
import com.example.helloworld.services.MessageService;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MessageHandler {

  private final MessageService messageService;

  /**
   * @param request required by the router handler
   */
  public ServerResponse getPublic(final ServerRequest request) {
    final var message = messageService.getPublicMessage();

    return ok().body(message);
  }

  /**
   * @param request required by the router handler
   */
  public ServerResponse getProtected(final ServerRequest request) {
    final var message = messageService.getProtectedMessage();

    return ok().body(message);
  }

  @CanReadMessages
  public ServerResponse getAdmin(final ServerRequest request) {
    final var message = messageService.getAdminMessage();

    return ok().body(message);
  }
}
