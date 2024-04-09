package com.clinic.xadmin.security.context;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Component;

@Component
public class AppSecurityContextHolder {

  // Current thread which holds Security Context

  private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

  public SecurityContext createContext(Authentication authentication) {
    SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
    context.setAuthentication(authentication);
    return context;
  }

  public void setContext(SecurityContext context) {
    this.securityContextHolderStrategy.setContext(context);
  }

  public SecurityContext getCurrentContext() {
    return this.securityContextHolderStrategy.getContext();
  }

}
