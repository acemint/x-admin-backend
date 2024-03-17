package com.clinic.xadmin.security.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;

import java.util.Arrays;


@Configuration
public class AuthenticationManagerConfiguration {

  public static final String AUTHENTICATION_MANAGER_BEAN_NAME = "AuthenticationManager";
  private final AuthenticationProvider daoAuthenticationProvider;

  @Autowired
  public AuthenticationManagerConfiguration(
      @Qualifier(value = AuthenticationProviderConfiguration.DAO_AUTHENTICATION_PROVIDER_BEAN_NAME) AuthenticationProvider daoAuthenticationProvider) {
    this.daoAuthenticationProvider = daoAuthenticationProvider;
  }

  @Bean(value = AuthenticationManagerConfiguration.AUTHENTICATION_MANAGER_BEAN_NAME)
  public AuthenticationManager authenticationManager() {
    return new ProviderManager(Arrays.asList(this.daoAuthenticationProvider));
  }

}
