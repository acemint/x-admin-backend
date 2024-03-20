package com.clinic.xadmin.controller.security;


import com.clinic.xadmin.controller.header.XAdminAuthorizationHeader;
import com.clinic.xadmin.controller.security.dto.request.LoginEmployeeRequest;
import com.clinic.xadmin.controller.security.dto.request.RegisterEmployeeRequest;
import com.clinic.xadmin.controller.security.dto.response.EmployeeResponse;
import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.security.configuration.AuthenticationManagerConfiguration;
import com.clinic.xadmin.security.util.JwtTokenUtil;
import com.clinic.xadmin.security.util.JwtTokenUtilImpl;
import com.clinic.xadmin.service.employee.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = SecurityControllerPath.BASE)
public class SecurityController {
  private final AuthenticationManager authenticationManager;
  private final JwtTokenUtil jwtTokenUtil;
  private final EmployeeService employeeService;

  @Autowired
  public SecurityController(
      EmployeeService employeeService,
      @Qualifier(value = AuthenticationManagerConfiguration.AUTHENTICATION_MANAGER_BEAN_NAME)  AuthenticationManager authenticationManager,
      @Qualifier(value = JwtTokenUtilImpl.BEAN_NAME) JwtTokenUtil jwtTokenUtil) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenUtil = jwtTokenUtil;
    this.employeeService = employeeService;
  }

  @PostMapping(value = SecurityControllerPath.LOGIN)
  public ResponseEntity<String> login(@RequestBody LoginEmployeeRequest request) {
    Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(request.getEmail(), request.getPassword());
    Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);

    return ResponseEntity.ok()
        .header(XAdminAuthorizationHeader.NAME, this.jwtTokenUtil.generateJwtToken(authenticationResponse))
        .body("Successfully Authenticated");
  }

  @PostMapping(value = SecurityControllerPath.REGISTER)
  @PreAuthorize("hasAnyRole('DEVELOPER', 'ADMIN')")
  public ResponseEntity<EmployeeResponse> register(@RequestBody RegisterEmployeeRequest request) {
    Employee employee = this.employeeService.createEmployee(request);
    return ResponseEntity.ok().body(EmployeeResponse.builder()
            .email(employee.getEmailAddress())
            .firstName(employee.getFirstName())
            .lastName(employee.getLastName())
            .phoneNumber(employee.getPhoneNumber())
        .build());
  }

}
