package com.clinic.xadmin.entity.audit;

import com.clinic.xadmin.entity.Member;
import com.clinic.xadmin.security.authprovider.CustomUserDetails;
import com.clinic.xadmin.security.context.AppSecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public class AuditAware implements AuditorAware<String> {

  private final AppSecurityContextHolder appSecurityContextHolder;

  @Autowired
  public AuditAware(AppSecurityContextHolder appSecurityContextHolder) {
    this.appSecurityContextHolder = appSecurityContextHolder;
  }

  @Override
  public Optional<String> getCurrentAuditor() {
    Authentication authentication = this.appSecurityContextHolder.getCurrentContext().getAuthentication();
    Member currentAuthenticatedUser = ((CustomUserDetails) authentication.getPrincipal()).getMember();
    return Optional.ofNullable(currentAuthenticatedUser)
        .map(Member::getUsername);
  }
}
