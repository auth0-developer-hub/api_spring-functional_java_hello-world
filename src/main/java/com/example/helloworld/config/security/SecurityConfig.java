package com.example.helloworld.config.security;

import static com.example.helloworld.config.Router.ADMIN_PATTERN;
import static com.example.helloworld.config.Router.PROTECTED_PATTERN;
import static org.springframework.security.oauth2.core.OAuth2ErrorCodes.INVALID_TOKEN;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.example.helloworld.config.ApplicationProperties;
import com.example.helloworld.config.ErrorHandler;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.cors.CorsConfiguration;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final ErrorHandler errorHandler;

  private final OAuth2ResourceServerProperties resourceServerProps;

  private final ApplicationProperties applicationProps;

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http.authorizeRequests()
      .antMatchers(PROTECTED_PATTERN, ADMIN_PATTERN)
        .authenticated()
      .anyRequest()
        .permitAll()
      .and()
        .cors()
          .configurationSource(this::corsSource)
      .and()
        .oauth2ResourceServer()
          .authenticationEntryPoint(errorHandler::handleAuthenticationError)
          .jwt()
            .decoder(jwtDecoder());
  }

  private CorsConfiguration corsSource(final HttpServletRequest request) {
    final var corsConfig = new CorsConfiguration();
    corsConfig.applyPermitDefaultValues();
    corsConfig.setAllowedOrigins(List.of(applicationProps.getClientOriginUrl()));

    return corsConfig;
  }

  private JwtDecoder jwtDecoder() {
    final var issuer = resourceServerProps.getJwt().getIssuerUri();
    final var decoder = JwtDecoders.<NimbusJwtDecoder>fromIssuerLocation(issuer);
    final var withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
    final var tokenValidator = new DelegatingOAuth2TokenValidator<>(withIssuer, this::withAudience);

    decoder.setJwtValidator(tokenValidator);
    return decoder;
  }

  private OAuth2TokenValidatorResult withAudience(final Jwt token) {
    final var audienceError = new OAuth2Error(
      INVALID_TOKEN,
      "The token was not issued for the given audience",
      "https://datatracker.ietf.org/doc/html/rfc6750#section-3.1"
    );

    return token.getAudience().contains(applicationProps.getAudience())
      ? OAuth2TokenValidatorResult.success()
      : OAuth2TokenValidatorResult.failure(audienceError);
  }
}
