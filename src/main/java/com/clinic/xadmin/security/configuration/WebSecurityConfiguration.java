package com.clinic.xadmin.security.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfigurationSource;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
    prePostEnabled = true
)
public class WebSecurityConfiguration {

  private final AuthenticationProvider daoAuthenticationProvider;
  private final CorsConfigurationSource corsConfigurationSource;
  private final SecurityContextRepository securityContextRepository;

  @Autowired
  public WebSecurityConfiguration(
      @Qualifier(value = AuthenticationProviderConfiguration.DAO_AUTHENTICATION_PROVIDER_BEAN_NAME) AuthenticationProvider daoAuthenticationProvider,
      @Qualifier(value = CustomCorsConfiguration.CORS_CONFIGURATION_BEAN_NAME) CorsConfigurationSource corsConfigurationSource,
      @Qualifier(value = SessionManagementConfiguration.DEFAULT_SESSION_MANAGEMENT_REPOSITORY_BEAN_NAME) SecurityContextRepository securityContextRepository) {
    this.daoAuthenticationProvider = daoAuthenticationProvider;
    this.corsConfigurationSource = corsConfigurationSource;
    this.securityContextRepository = securityContextRepository;
  }


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // TODO: implement CSRF for non-"safe" methods (https://docs.spring.io/spring-security/reference/features/exploits/csrf.html)
    http.csrf(csrf -> csrf.disable());
    http.cors(cors -> cors.configurationSource(corsConfigurationSource));
    http.securityContext((securityContext) -> securityContext.securityContextRepository(securityContextRepository));
    http.authenticationProvider(daoAuthenticationProvider);
    return http.build();
  }

}
