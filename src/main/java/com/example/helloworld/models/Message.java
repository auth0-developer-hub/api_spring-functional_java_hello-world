package com.example.helloworld.models;

public record Message(Metadata metadata, String text) {

  public static Message from(final String text) {
    return new Message(Metadata.init(), text);
  }

  private static record Metadata(String api, String branch) {

    public static Metadata init() {
      return new Metadata(
        "api_spring-functional_java_hello-world",
        "basic-role-based-access-control"
      );
    }
  }
}
