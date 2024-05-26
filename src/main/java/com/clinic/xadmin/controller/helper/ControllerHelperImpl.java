package com.clinic.xadmin.controller.helper;

import com.clinic.xadmin.constant.member.MemberRole;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Member;
import com.clinic.xadmin.exception.XAdminBadRequestException;
import com.clinic.xadmin.exception.XAdminForbiddenException;
import com.clinic.xadmin.exception.XAdminIllegalStateException;
import com.clinic.xadmin.repository.clinic.ClinicRepository;
import com.clinic.xadmin.security.authprovider.CustomUserDetails;
import com.clinic.xadmin.security.context.AppSecurityContextHolder;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

@Service
public class ControllerHelperImpl implements ControllerHelper {

  private final AppSecurityContextHolder appSecurityContextHolder;
  private final ClinicRepository clinicRepository;

  @Autowired
  public ControllerHelperImpl(AppSecurityContextHolder appSecurityContextHolder, ClinicRepository clinicRepository) {
    this.appSecurityContextHolder = appSecurityContextHolder;
    this.clinicRepository = clinicRepository;
  }

  @Nonnull
  @Override
  public Clinic getClinicScope(String clinicCode) {
    Authentication authentication = this.appSecurityContextHolder.getCurrentContext().getAuthentication();
    if (authentication.getAuthorities().stream().anyMatch(g -> g.getAuthority().contains("ROLE_ANONYMOUS"))) {
      throw new XAdminForbiddenException("This API expects an authenticated user");
    }
    Member currentAuthenticatedUser = ((CustomUserDetails) authentication.getPrincipal()).getMember();
    return this.getAccordingToRole(currentAuthenticatedUser, clinicCode);
  }

  private Clinic getAccordingToRole(Member currentAuthenticatedUser, String clinicCode) {
    if (MemberRole.isFirefighterRoles(currentAuthenticatedUser)) {
      return this.getByManagerRole(clinicCode);
    }
    else {
      return this.getByNonManagerRole(currentAuthenticatedUser, clinicCode);
    }
  }

  private Clinic getByNonManagerRole(Member currentAuthenticatedUser, String clinicCode) {
    if (StringUtils.hasText(clinicCode)) {
      throw new XAdminForbiddenException("Clinic code request param cannot be passed for user role " + currentAuthenticatedUser.getRole() + " for request parameter " + clinicCode);
    }
    if (Objects.isNull(currentAuthenticatedUser.getClinic())) {
      throw new XAdminIllegalStateException("Unable to get user role " + currentAuthenticatedUser.getRole() + " without clinic id" + " for request parameter " + clinicCode);
    }
    return currentAuthenticatedUser.getClinic();
  }

  private Clinic getByManagerRole(String clinicCode) {
    if (StringUtils.hasText(clinicCode)) {
      return Optional.ofNullable(this.clinicRepository.searchByCode(clinicCode))
          .orElseThrow(() -> new XAdminBadRequestException("Clinic code " + clinicCode + " not found"));
    }
    throw new XAdminIllegalStateException("The corresponding controller should have clinic code as it is hit with Firefighter Roles, request for changes if it is otherwise wrong" + " for request parameter " + clinicCode);
  }

}
