package com.github.driversti.erepublik.auth.config;

import com.github.driversti.erepublik.auth.AuthenticationClient;
import com.github.driversti.erepublik.auth.ProdAuthenticationClient;
import com.github.driversti.erepublik.auth.RestTemplateResponseErrorHandler;
import java.net.Authenticator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConditionalOnClass(Authenticator.class)
public class AuthAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public RestTemplate restTemplate() {
    return new RestTemplateBuilder()
        .errorHandler(new RestTemplateResponseErrorHandler())
        .build();
  }

  @Bean
  public AuthenticationClient authenticationClient(@Value("${app.erepublik.email}") String email,
      @Value("${app.erepublik.password}") String password, RestTemplate restTemplate) {
    return new ProdAuthenticationClient(email, password, restTemplate);
  }
}
