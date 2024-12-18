package com.clinic.xadmin.security.context;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Component;

@Component
public class AppSecurityContextHolder {

  // Class to hold Security Context

  private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

  public SecurityContext createContext(Authentication authentication) {
    SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
    context.setAuthentication(authentication);
    securityContextHolderStrategy.setContext(context);
    return context;
  }

  public SecurityContext getCurrentContext() {
    return this.securityContextHolderStrategy.getContext();
  }

}
