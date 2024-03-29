package com.clinic.xadmin.security.context;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Component;

@Component
public class AppSecurityContextHolder {

  private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

  public SecurityContext createEmptyContext() {
    return this.securityContextHolderStrategy.createEmptyContext();
  }

  public void setContext(SecurityContext context) {
    this.securityContextHolderStrategy.setContext(context);
  }

  public SecurityContext getCurrentContext() {
    return this.securityContextHolderStrategy.getContext();
  }

}
