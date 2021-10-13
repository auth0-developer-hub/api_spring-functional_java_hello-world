# Hello World API: Spring Boot + Java Sample

This repository contains a Sprint Boot project that defines an API server using Java and Gradle. You'll secure this API with Auth0 to practice making secure API calls from a client application.

##  Starting the server

You can start the server in a terminal using the command:

```sh
./gradlew bootRun
```

It's also possible to start the server using [Spring Tools](https://spring.io/tools) and the IDE of your preference (VSCode, Eclipse, etc)

## Development

This section is intended for development and contribution to this repository.

### Dependencies

Dependencies are managed by Gradle and their versions are locked so we can ensure reproducible builds. If you need to include another dependencie or plugin, you can add them to the `build.gradle` file as always, and the you'll need to run the following command to update the lock files:

```sh
./gradlew dependencies --write-locks
```

### Checking

The check task will run `Checkstyle` and `Sonarlint` task for both the main and test sources. After this tasks the test will be executed too. You can run the check task with the command:

```sh
./gradlew check
```

### Testing

Test can also be run on their own without the other tasks that come with the check task. To run all tests you can use:

```sh
./gradlew test
```
