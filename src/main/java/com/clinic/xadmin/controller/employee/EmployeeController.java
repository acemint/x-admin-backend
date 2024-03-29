package com.clinic.xadmin.controller.employee;


import com.clinic.xadmin.controller.constant.SecurityAuthorizationType;
import com.clinic.xadmin.dto.request.employee.LoginEmployeeRequest;
import com.clinic.xadmin.dto.request.employee.RegisterEmployeeRequest;
import com.clinic.xadmin.dto.request.employee.ResetPasswordRequest;
import com.clinic.xadmin.dto.response.employee.EmployeeResponse;
import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.mapper.EmployeeResponseMapper;
import com.clinic.xadmin.model.employee.EmployeeFilter;
import com.clinic.xadmin.security.authprovider.CustomUserDetails;
import com.clinic.xadmin.security.configuration.AuthenticationManagerConfiguration;
import com.clinic.xadmin.security.configuration.SessionManagementConfiguration;
import com.clinic.xadmin.security.context.AppSecurityContextHolder;
import com.clinic.xadmin.service.employee.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = EmployeeControllerPath.BASE)
public class EmployeeController {
  private final AuthenticationManager authenticationManager;
  private final EmployeeService employeeService;
  private final SecurityContextRepository securityContextRepository;

  private AppSecurityContextHolder appSecurityContextHolder;

  @Autowired
  public EmployeeController(
      EmployeeService employeeService,
      @Qualifier(value = AuthenticationManagerConfiguration.AUTHENTICATION_MANAGER_BEAN_NAME)  AuthenticationManager authenticationManager,
      @Qualifier(value = SessionManagementConfiguration.DEFAULT_SESSION_MANAGEMENT_REPOSITORY_BEAN_NAME)  SecurityContextRepository securityContextRepository,
      AppSecurityContextHolder appSecurityContextHolder) {
    this.authenticationManager = authenticationManager;
    this.employeeService = employeeService;
    this.securityContextRepository = securityContextRepository;
    this.appSecurityContextHolder = appSecurityContextHolder;
  }

  @Operation(
      summary = EmployeeControllerDocs.LOGIN_SUMMARY,
      description = EmployeeControllerDocs.LOGIN_DESCRIPTION)
  @PostMapping(value = EmployeeControllerPath.LOGIN, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<EmployeeResponse> login(@RequestBody LoginEmployeeRequest request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
    Authentication authenticationResponse = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmailAddress(), request.getPassword()));

    SecurityContext context = this.appSecurityContextHolder.createEmptyContext();
    context.setAuthentication(authenticationResponse);
    this.appSecurityContextHolder.setContext(context);
    this.securityContextRepository.saveContext(context, httpServletRequest, httpServletResponse);

    CustomUserDetails userDetails =(CustomUserDetails) authenticationResponse.getPrincipal();
    return ResponseEntity.ok().body(EmployeeResponseMapper.INSTANCE.createFrom(userDetails.getEmployee()));
  }

  @Operation(
      summary = EmployeeControllerDocs.REGISTER_SUMMARY,
      description = EmployeeControllerDocs.REGISTER_DESCRIPTION)
  @PostMapping(value = EmployeeControllerPath.REGISTER, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(SecurityAuthorizationType.IS_ADMIN_OR_DEVELOPER)
  public ResponseEntity<EmployeeResponse> register(@RequestBody @Valid RegisterEmployeeRequest request) {
    Employee employee = this.employeeService.createEmployee(request);
    return ResponseEntity.ok().body(
        EmployeeResponseMapper.INSTANCE.createFrom(employee));
  }

  @Operation(
      summary = EmployeeControllerDocs.GET_SELF_SUMMARY)
  @GetMapping(value = EmployeeControllerPath.SELF, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(SecurityAuthorizationType.IS_FULLY_AUTHENTICATED)
  public ResponseEntity<EmployeeResponse> getSelf() {
    CustomUserDetails userDetails = (CustomUserDetails) this.appSecurityContextHolder.getCurrentContext().getAuthentication().getPrincipal();
    return ResponseEntity.ok().body(
        EmployeeResponseMapper.INSTANCE.createFrom(userDetails.getEmployee()));
  }

  @Operation(
      summary = EmployeeControllerDocs.GET_EMPLOYEES_SUMMARY,
      description = EmployeeControllerDocs.GET_EMPLOYEES_DESCRIPTION)
  @GetMapping(value = EmployeeControllerPath.FILTER, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(SecurityAuthorizationType.IS_FULLY_AUTHENTICATED)
  public ResponseEntity<Page<EmployeeResponse>> getListEmployeesByName(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) List<String> sortBy,
      @RequestParam(defaultValue = EmployeeControllerDefaultValue.DEFAULT_SORT_ORDER) String sortDirection,
      @RequestParam(defaultValue = EmployeeControllerDefaultValue.DEFAULT_PAGE_NUMBER) Integer pageNumber,
      @RequestParam(defaultValue = EmployeeControllerDefaultValue.DEFAULT_PAGE_SIZE) Integer pageSize) {
    PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
    if (!Objects.isNull(sortBy)) {
      pageRequest.withSort(Sort.Direction.valueOf(sortDirection), sortBy.toArray(new String[0]));
    }

    EmployeeFilter employeeFilter = EmployeeFilter.builder()
        .name(name)
        .pageable(pageRequest)
        .build();
    Page<Employee> employees = this.employeeService.getEmployees(employeeFilter);
    List<EmployeeResponse> employeeResponses = EmployeeResponseMapper.INSTANCE.createFrom(employees.getContent());

    return ResponseEntity.ok().body(new PageImpl<>(employeeResponses, pageRequest, employees.getTotalElements()));
  }

}
