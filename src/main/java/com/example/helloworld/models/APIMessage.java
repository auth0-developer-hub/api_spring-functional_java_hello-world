package com.example.helloworld.models;

import lombok.Value;

@Value
public class APIMessage {

  private String message;

  public static APIMessage from(final String message) {
    return new APIMessage(message);
  }
}
