package com.clinic.xadmin.helper;

import com.clinic.xadmin.constant.employee.EmployeeRole;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.security.authprovider.CustomUserDetails;
import com.clinic.xadmin.security.authprovider.CustomUserDetailsFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Objects;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

  @Override
  public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();

    CustomUserDetails principal = CustomUserDetailsFactory.createFrom(
        Employee.builder()
            .emailAddress(customUser.username())
            .role(customUser.roles()[0])
            .build());

    if (!EmployeeRole.FIREFIGHTER_ROLES.containsKey(principal.getEmployee().getRole()) && Objects.isNull(customUser.clinicId())) {
      throw new IllegalStateException("Unable to create user role " + principal.getEmployee().getRole() + " without clinic id");
    } else {
      principal.getEmployee().setClinic(Clinic.builder()
          .id(customUser.clinicId())
          .code("CLC-" + customUser.clinicId())
          .build());
    }

    Authentication auth = UsernamePasswordAuthenticationToken.authenticated(principal, "password", principal.getAuthorities());
    context.setAuthentication(auth);

    return context;
  }
}