package com.clinic.xadmin.controller.root;

import com.clinic.xadmin.dto.request.member.LoginMemberRequest;
import com.clinic.xadmin.dto.response.StandardizedResponse;
import com.clinic.xadmin.dto.response.member.MemberResponse;
import com.clinic.xadmin.exception.XAdminBadRequestException;
import com.clinic.xadmin.exception.XAdminForbiddenException;
import com.clinic.xadmin.mapper.MemberMapper;
import com.clinic.xadmin.security.authprovider.CustomUserDetails;
import com.clinic.xadmin.security.configuration.AuthenticationManagerConfiguration;
import com.clinic.xadmin.security.configuration.SessionManagementConfiguration;
import com.clinic.xadmin.security.context.AppSecurityContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RootPublicControllerPath.BASE)
public class RootPublicController {

  private final AuthenticationManager authenticationManager;
  private final SecurityContextRepository securityContextRepository;

  private final AppSecurityContextHolder appSecurityContextHolder;

  @Autowired
  public RootPublicController(
      @Qualifier(value = AuthenticationManagerConfiguration.AUTHENTICATION_MANAGER_BEAN_NAME)  AuthenticationManager authenticationManager,
      @Qualifier(value = SessionManagementConfiguration.DEFAULT_SESSION_MANAGEMENT_REPOSITORY_BEAN_NAME)  SecurityContextRepository securityContextRepository,
      AppSecurityContextHolder appSecurityContextHolder) {
    this.authenticationManager = authenticationManager;
    this.securityContextRepository = securityContextRepository;
    this.appSecurityContextHolder = appSecurityContextHolder;
  }

  @Operation(
      summary = RootPublicControllerDocs.LOGIN_SUMMARY,
      description = RootPublicControllerDocs.LOGIN_DESCRIPTION)
  @PostMapping(value = RootPublicControllerPath.LOGIN, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<StandardizedResponse<MemberResponse>> login(@RequestBody LoginMemberRequest request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
    Authentication authenticationResponse = null;
    try {
      authenticationResponse = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    } catch (IllegalArgumentException e) {
      throw new XAdminForbiddenException("Current user does not have eligibility to login");
    }

    SecurityContext context = this.appSecurityContextHolder.createContext(authenticationResponse);
    this.securityContextRepository.saveContext(context, httpServletRequest, httpServletResponse);

    CustomUserDetails userDetails = (CustomUserDetails) authenticationResponse.getPrincipal();
    return ResponseEntity.ok().body(
        StandardizedResponse.<MemberResponse>builder()
            .content(MemberMapper.INSTANCE.createFrom(userDetails.getMember()))
            .build());
  }

  @GetMapping(value = RootPublicControllerPath.HEALTH, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<StandardizedResponse<String>> health() {
    return ResponseEntity.ok().body(
        StandardizedResponse.<String>builder()
            .content("Successful")
            .build());
  }

}
