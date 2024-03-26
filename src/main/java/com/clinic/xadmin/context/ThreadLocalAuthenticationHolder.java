package com.clinic.xadmin.context;

import org.springframework.security.core.Authentication;

public class ThreadLocalAuthenticationHolder {

  public final static ThreadLocal<Authentication> authentication = new ThreadLocal<>();

}
