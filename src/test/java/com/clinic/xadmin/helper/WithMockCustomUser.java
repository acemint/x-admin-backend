package com.clinic.xadmin.helper;

import com.clinic.xadmin.constant.EmployeeRole;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

  String username() default "username";

  String[] roles() default {EmployeeRole.ROLE_REGULAR_EMPLOYEE};
}
