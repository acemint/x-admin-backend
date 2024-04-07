package com.clinic.xadmin.controller.root;

import com.clinic.xadmin.dto.request.employee.LoginEmployeeRequest;
import com.clinic.xadmin.dto.response.StandardizedResponse;
import com.clinic.xadmin.dto.response.employee.EmployeeResponse;
import com.clinic.xadmin.mapper.EmployeeMapper;
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
  public ResponseEntity<StandardizedResponse<EmployeeResponse>> login(@RequestBody LoginEmployeeRequest request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
    Authentication
        authenticationResponse = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

    SecurityContext context = this.appSecurityContextHolder.createEmptyContext();
    context.setAuthentication(authenticationResponse);
    this.appSecurityContextHolder.setContext(context);
    this.securityContextRepository.saveContext(context, httpServletRequest, httpServletResponse);

    CustomUserDetails userDetails = (CustomUserDetails) authenticationResponse.getPrincipal();
    return ResponseEntity.ok().body(
        StandardizedResponse.<EmployeeResponse>builder()
            .content(EmployeeMapper.INSTANCE.createFrom(userDetails.getEmployee()))
            .build());
  }

}