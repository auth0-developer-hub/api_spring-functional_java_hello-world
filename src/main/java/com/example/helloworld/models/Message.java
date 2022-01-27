package com.example.helloworld.models;

public record Message(Metadata metadata, String text) {

  public static Message from(final String text) {
    return new Message(Metadata.ofDefaults(), text);
  }

  private static record Metadata(String api, String branch) {

    public static Metadata ofDefaults() {
      return new Metadata(
        "api_spring-functional_java_hello-world",
        "starter"
      );
    }
  }
}
