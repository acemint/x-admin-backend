package com.clinic.xadmin.helper;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

  String username() default WithMockCustomUserConstants.DEFAULT_USERNAME;

  String[] roles() default { WithMockCustomUserConstants.DEFAULT_ROLES };

  String clinicId() default WithMockCustomUserConstants.DEFAULT_CLINIC_ID;
}
