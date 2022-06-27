package com.example.helloworld.config.security;

import com.example.helloworld.config.ApplicationProperties;
import com.example.helloworld.config.GlobalErrorHandler;
import com.example.helloworld.config.Paths;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final GlobalErrorHandler errorHandler;

  private final OAuth2ResourceServerProperties resourceServerProps;

  private final ApplicationProperties applicationProps;

  @Override
  public void configure(final WebSecurity web) throws Exception {
    final var messages = Paths.apiPath().messagesPath();
    final var exclusionRegex = "^(?!%s|%s).*$".formatted(
      messages.protectedPath().build(),
      messages.adminPath().build()
    );

    web.ignoring()
      .regexMatchers(exclusionRegex);
  }

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    final var messages = Paths.apiPath().messagesPath();

    http.authorizeRequests(authorizeRequests ->
      authorizeRequests
        .antMatchers(messages.protectedPath().build(), messages.adminPath().build())
          .authenticated()
        .anyRequest()
          .permitAll()
    )
    .cors(Customizer.withDefaults())
    .oauth2ResourceServer(oauth2ResourceServer ->
      oauth2ResourceServer
        .authenticationEntryPoint(errorHandler::handleAuthenticationError)
        .jwt(jwt -> jwt.decoder(makeJwtDecoder()))
    );
  }

  private JwtDecoder makeJwtDecoder() {
    final var issuer = resourceServerProps.getJwt().getIssuerUri();
    final var decoder = JwtDecoders.<NimbusJwtDecoder>fromIssuerLocation(issuer);
    final var withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
    final var tokenValidator = new DelegatingOAuth2TokenValidator<>(withIssuer, this::withAudience);

    decoder.setJwtValidator(tokenValidator);
    return decoder;
  }

  private OAuth2TokenValidatorResult withAudience(final Jwt token) {
    final var audienceError = new OAuth2Error(
      OAuth2ErrorCodes.INVALID_TOKEN,
      "The token was not issued for the given audience",
      "https://datatracker.ietf.org/doc/html/rfc6750#section-3.1"
    );

    return token.getAudience().contains(applicationProps.audience())
      ? OAuth2TokenValidatorResult.success()
      : OAuth2TokenValidatorResult.failure(audienceError);
  }
}
