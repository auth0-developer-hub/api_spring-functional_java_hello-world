package com.example.helloworld.handlers;

import static org.springframework.web.servlet.function.ServerResponse.ok;

import com.example.helloworld.config.security.CanReadMessages;
import com.example.helloworld.models.APIMessage;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Component
public class MessageHandler {

  /**
   * @param request required by the router handler
   */
  public ServerResponse publicMessage(final ServerRequest request) {
    final var message = "The API doesn't require an access token to share this message.";

    return ok()
      .body(APIMessage.from(message));
  }

  /**
   * @param request required by the router handler
   */
  public ServerResponse protectedMessage(final ServerRequest request) {
    final var message = "The API successfully validated your access token.";

    return ok()
      .body(APIMessage.from(message));
  }

  @CanReadMessages
  public ServerResponse adminMessage(final ServerRequest request) {
    final var message = "The API successfully recognized you as an admin.";

    return ok()
      .body(APIMessage.from(message));
  }
}
