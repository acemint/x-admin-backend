package com.clinic.xadmin.security.configuration;

import com.clinic.xadmin.security.authprovider.CustomUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class AuthenticationProviderConfiguration {
  public static final String DAO_AUTHENTICATION_PROVIDER_BEAN_NAME = "DaoAuthenticationProvider";

  private final UserDetailsService userDetailsService;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public AuthenticationProviderConfiguration(
      @Qualifier(value = CustomUserDetailsServiceImpl.BEAN_NAME) UserDetailsService userDetailsService,
      @Qualifier(value = PasswordEncoderConfiguration.BEAN_NAME) PasswordEncoder passwordEncoder) {
    this.userDetailsService = userDetailsService;
    this.passwordEncoder = passwordEncoder;
  }

  @Bean(value = AuthenticationProviderConfiguration.DAO_AUTHENTICATION_PROVIDER_BEAN_NAME)
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder);
    return authProvider;
  }

}
