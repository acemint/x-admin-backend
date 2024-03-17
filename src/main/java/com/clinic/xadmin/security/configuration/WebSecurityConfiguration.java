package com.clinic.xadmin.security.configuration;

import com.clinic.xadmin.constant.EmployeeRole;
import com.clinic.xadmin.controller.security.SecurityControllerPath;
import com.clinic.xadmin.security.filter.JWTTokenRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;


@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

  private final AuthenticationProvider daoAuthenticationProvider;
  private final OncePerRequestFilter jwtTokenFilter;
  private final CorsConfigurationSource corsConfigurationSource;


  @Autowired
  public WebSecurityConfiguration(
      @Qualifier(value = AuthenticationProviderConfiguration.DAO_AUTHENTICATION_PROVIDER_BEAN_NAME) AuthenticationProvider daoAuthenticationProvider,
      @Qualifier(value = JWTTokenRequestFilter.JWT_TOKEN_FILTER_BEAN) OncePerRequestFilter jwtTokenFilter,
      @Qualifier(value = CustomCorsConfiguration.CORS_CONFIGURATION_BEAN_NAME) CorsConfigurationSource corsConfigurationSource) {
    this.daoAuthenticationProvider = daoAuthenticationProvider;
    this.jwtTokenFilter = jwtTokenFilter;
    this.corsConfigurationSource = corsConfigurationSource;
  }


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable());
    http.cors(cors -> cors.configurationSource(corsConfigurationSource));
    http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    http.authorizeHttpRequests((authorize) -> authorize
            .requestMatchers(HttpMethod.POST, SecurityControllerPath.BASE + SecurityControllerPath.LOGIN)
              .permitAll()
            .requestMatchers(HttpMethod.POST, SecurityControllerPath.BASE + SecurityControllerPath.REGISTER)
              .hasRole(EmployeeRole.SUPER_ADMIN.name())
            .requestMatchers(HttpMethod. GET, "/swagger-ui/**", "/v3/api-docs/**")
              .permitAll()
            .anyRequest().authenticated());
    http.authenticationProvider(daoAuthenticationProvider);
    http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

}
