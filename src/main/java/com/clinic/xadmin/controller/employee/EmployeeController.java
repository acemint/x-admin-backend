package com.clinic.xadmin.controller.employee;


import com.clinic.xadmin.controller.helper.ControllerHelper;
import com.clinic.xadmin.dto.request.employee.RegisterEmployeeRequest;
import com.clinic.xadmin.dto.response.StandardizedResponse;
import com.clinic.xadmin.dto.response.employee.EmployeeResponse;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.mapper.EmployeeMapper;
import com.clinic.xadmin.mapper.PaginationMapper;
import com.clinic.xadmin.model.employee.EmployeeFilter;
import com.clinic.xadmin.model.employee.RegisterEmployeeData;
import com.clinic.xadmin.security.authprovider.CustomUserDetails;
import com.clinic.xadmin.security.constant.SecurityAuthorizationType;
import com.clinic.xadmin.security.context.AppSecurityContextHolder;
import com.clinic.xadmin.service.employee.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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

  private final ControllerHelper controllerHelper;
  private final EmployeeService employeeService;
  private final AppSecurityContextHolder appSecurityContextHolder;

  @Autowired
  public EmployeeController(
      ControllerHelper controllerHelper,
      EmployeeService employeeService,
      AppSecurityContextHolder appSecurityContextHolder) {
    this.controllerHelper = controllerHelper;
    this.employeeService = employeeService;
    this.appSecurityContextHolder = appSecurityContextHolder;
  }

  @Operation(
      summary = EmployeeControllerDocs.REGISTER_SUMMARY,
      description = EmployeeControllerDocs.REGISTER_DESCRIPTION)
  @PostMapping(value = EmployeeControllerPath.REGISTER, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(SecurityAuthorizationType.IS_CLINIC_ADMIN)
  public ResponseEntity<StandardizedResponse<EmployeeResponse>> register(
      @RequestParam(name = "clinicCode", required = false) String clinicCode,
      @RequestBody @Valid RegisterEmployeeRequest request) {
    Clinic clinic = controllerHelper.getClinicScope(clinicCode);
    clinicCode = clinic.getCode();

    RegisterEmployeeData registerEmployeeData = EmployeeMapper.INSTANCE.convertFromDtoToModel(request);
    registerEmployeeData.setClinicCode(clinicCode);

    Employee employee = this.employeeService.createEmployee(registerEmployeeData);
    return ResponseEntity.ok().body(
        StandardizedResponse.<EmployeeResponse>builder()
            .content(EmployeeMapper.INSTANCE.createFrom(employee))
            .build());
  }

  @Operation(
      summary = EmployeeControllerDocs.GET_SELF_SUMMARY)
  @GetMapping(value = EmployeeControllerPath.SELF, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(SecurityAuthorizationType.IS_FULLY_AUTHENTICATED)
  public ResponseEntity<StandardizedResponse<EmployeeResponse>> getSelf() {
    CustomUserDetails userDetails = (CustomUserDetails) this.appSecurityContextHolder.getCurrentContext().getAuthentication().getPrincipal();
    return ResponseEntity.ok().body(
        StandardizedResponse.<EmployeeResponse>builder()
            .content(EmployeeMapper.INSTANCE.createFrom(userDetails.getEmployee()))
            .build());
  }

  @Operation(
      summary = EmployeeControllerDocs.GET_EMPLOYEES_SUMMARY,
      description = EmployeeControllerDocs.GET_EMPLOYEES_DESCRIPTION)
  @GetMapping(value = EmployeeControllerPath.FILTER, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(SecurityAuthorizationType.IS_FULLY_AUTHENTICATED)
  public ResponseEntity<StandardizedResponse<List<EmployeeResponse>>> filter(
      @RequestParam(name = "name", required = false) String name,
      @RequestParam(name = "clinicCode", required = false) String clinicCode,
      @RequestParam(name = "sortBy", required = false) String[] sortBy,
      @RequestParam(name = "sortDirection", defaultValue = EmployeeControllerDefaultValue.DEFAULT_SORT_ORDER) String sortDirection,
      @RequestParam(name = "pageNumber", defaultValue = EmployeeControllerDefaultValue.DEFAULT_PAGE_NUMBER) Integer pageNumber,
      @RequestParam(name = "pageSize", defaultValue = EmployeeControllerDefaultValue.DEFAULT_PAGE_SIZE) Integer pageSize) {
    Clinic clinic = controllerHelper.getClinicScope(clinicCode);
    clinicCode = clinic.getCode();

    PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
    if (!Objects.isNull(sortBy)) {
      Sort sort = Sort.by(Sort.Direction.valueOf(sortDirection), sortBy);
      pageRequest = pageRequest.withSort(sort);
    }

    EmployeeFilter employeeFilter = EmployeeFilter.builder()
        .name(name)
        .clinicCode(clinicCode)
        .pageable(pageRequest)
        .build();
    Page<Employee> employees = this.employeeService.getEmployees(employeeFilter);

    return ResponseEntity.ok().body(StandardizedResponse
        .<List<EmployeeResponse>>builder()
        .content(EmployeeMapper.INSTANCE.createFrom(employees.getContent()))
        .paginationMetadata(PaginationMapper.INSTANCE.createFrom(employees))
        .build());
  }

}
