package com.clinic.xadmin.controller;

import com.clinic.xadmin.constant.employee.EmployeeRole;
import com.clinic.xadmin.constant.employee.EmployeeType;
import com.clinic.xadmin.controller.employee.EmployeeControllerPath;
import com.clinic.xadmin.dto.request.employee.RegisterEmployeeRequest;
import com.clinic.xadmin.dto.response.employee.EmployeeResponse;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.helper.IntegrationTestHelper;
import com.clinic.xadmin.helper.WithMockCustomUser;
import com.clinic.xadmin.repository.clinic.ClinicRepository;
import com.clinic.xadmin.repository.employee.EmployeeRepository;
import com.clinic.xadmin.security.authprovider.CustomUserDetails;
import com.clinic.xadmin.security.configuration.PasswordEncoderConfiguration;
import com.clinic.xadmin.security.context.AppSecurityContextHolder;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Set;

public class EmployeeControllerTest extends BaseControllerTest {

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private ClinicRepository clinicRepository;

  @Autowired
  @Qualifier(value = PasswordEncoderConfiguration.BEAN_NAME)
  private PasswordEncoder passwordEncoder;

  @Autowired
  private AppSecurityContextHolder appSecurityContextHolder;

  @BeforeEach
  public void beforeEach() {

  }

  @AfterEach
  public void afterEach() {
    this.employeeRepository.deleteAll();
    this.clinicRepository.deleteAll();
  }

  private Employee constructBasicEmployee() {
    return Employee.builder()
        .emailAddress("user@gmail.com")
        .password(this.passwordEncoder.encode("Test123:>"))
        .role(EmployeeRole.ROLE_REGULAR_EMPLOYEE)
        .build();
  }

  private Clinic constructBasicClinic() {
    return Clinic.builder()
        .id("123")
        .build();
  }

  private Set<Employee> constructBasicEmployeesFromClinic(Clinic clinic) {
    return Set.of(
        Employee.builder()
            .firstName("user1")
            .emailAddress("user1@gmail.com")
            .password(this.passwordEncoder.encode("Test123:>"))
            .role(EmployeeRole.ROLE_REGULAR_EMPLOYEE)
            .clinic(clinic)
            .build(),
        Employee.builder()
            .firstName("user2")
            .emailAddress("user2@gmail.com")
            .password(this.passwordEncoder.encode("Test123:>"))
            .role(EmployeeRole.ROLE_REGULAR_EMPLOYEE)
            .clinic(clinic)
            .build(),
        Employee.builder()
            .firstName("user3")
            .emailAddress("user3@gmail.com")
            .password(this.passwordEncoder.encode("Test123:>"))
            .role(EmployeeRole.ROLE_REGULAR_EMPLOYEE)
            .clinic(clinic)
            .build()
    );
  }

  @Test
  public void login_Valid_IsOk() throws Exception {
    Employee employee = this.constructBasicEmployee();
    this.employeeRepository.save(employee);

    byte[] requestBody = IntegrationTestHelper
        .readJsonAsBytes("employee_login_normalUser.json", "json", "request");

    EmployeeResponse expectedResponseBody = IntegrationTestHelper
        .readJsonFile("employee_login.json", EmployeeResponse.class,  "json", "response");

    this.mockMvc.perform(MockMvcRequestBuilders.post(EmployeeControllerPath.BASE + EmployeeControllerPath.LOGIN)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content.emailAddress").value(expectedResponseBody.getEmailAddress()));
  }

  @Test
  public void login_InvalidPassword_IsForbidden() throws Exception {
    Employee employee = this.constructBasicEmployee();
    this.employeeRepository.save(employee);

    byte[] requestBody = IntegrationTestHelper
        .readJsonAsBytes("employee_login_normalUserWrongPassword.json", "json", "request");

    this.mockMvc.perform(MockMvcRequestBuilders.post(EmployeeControllerPath.BASE + EmployeeControllerPath.LOGIN)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
  }

  @Test
  @WithMockCustomUser()
  public void getSelf_EmployeeHasLoggedIn_IsOk() throws Exception {
    Authentication authentication = this.appSecurityContextHolder.getCurrentContext().getAuthentication();
    CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

    this.mockMvc.perform(MockMvcRequestBuilders.get(EmployeeControllerPath.BASE + EmployeeControllerPath.SELF)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content.emailAddress").value(customUserDetails.getEmployee().getEmailAddress()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content.role").value(customUserDetails.getEmployee().getRole()));
  }

  @Test
  public void getSelf_EmployeeHasNotLoggedIn_IsForbidden() throws Exception {
    this.mockMvc.perform(MockMvcRequestBuilders.get(EmployeeControllerPath.BASE + EmployeeControllerPath.SELF)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
  }

  @Test
  @WithMockCustomUser(clinicId = "234")
  public void filter_EmployeeCannotAccessOtherClinic_IsOk() throws Exception {
    Clinic clinic = this.constructBasicClinic();
    this.clinicRepository.save(clinic);
    Set<Employee> employees = this.constructBasicEmployeesFromClinic(clinic);
    this.employeeRepository.saveAll(employees);

    this.mockMvc.perform(MockMvcRequestBuilders.get(EmployeeControllerPath.BASE + EmployeeControllerPath.FILTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(0)));
  }

  @Test
  @WithMockCustomUser(clinicId = "123")
  public void filter_EmployeeAccessOwnClinicData_IsOk() throws Exception {
    Clinic clinic = this.constructBasicClinic();
    this.clinicRepository.save(clinic);
    Set<Employee> employees = this.constructBasicEmployeesFromClinic(clinic);
    this.employeeRepository.saveAll(employees);

    this.mockMvc.perform(MockMvcRequestBuilders.get(EmployeeControllerPath.BASE + EmployeeControllerPath.FILTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(3)));
  }

  @Test
  @WithMockCustomUser(roles = { EmployeeRole.ROLE_ADMIN })
  public void register_EmployeeRoleIsNotRegularEmployee_IsOk() throws Exception {
    Clinic clinic = this.constructBasicClinic();
    this.clinicRepository.save(clinic);
    byte[] requestBody = IntegrationTestHelper
        .readJsonAsBytes("employee_register_normalUser.json", "json", "request");

    this.mockMvc.perform(MockMvcRequestBuilders.post(EmployeeControllerPath.BASE + EmployeeControllerPath.REGISTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.notNullValue()));
    List<Employee> savedEmployees = this.employeeRepository.findAll();
    Assertions.assertEquals(savedEmployees.size(), 1);
    Assertions.assertEquals(savedEmployees.get(0).getClinic().getId(), "123");
    Assertions.assertEquals(savedEmployees.get(0).getEmailAddress(), "master@gmail.com");
  }

  @Test
  @WithMockCustomUser(roles = { EmployeeRole.ROLE_REGULAR_EMPLOYEE })
  public void register_EmployeeRoleIsRegularEmployee_IsForbidden() throws Exception {
    byte[] requestBody = IntegrationTestHelper
        .readJsonAsBytes("employee_register_normalUser.json", "json", "request");

    this.mockMvc.perform(MockMvcRequestBuilders.post(EmployeeControllerPath.BASE + EmployeeControllerPath.REGISTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
  }

  @Test
  @WithMockCustomUser(roles = { EmployeeRole.ROLE_ADMIN })
  public void register_RequestBodyEmailAddressIsInvalid_IsBadRequest() throws Exception {
    Clinic clinic = this.constructBasicClinic();
    this.clinicRepository.save(clinic);

    RegisterEmployeeRequest requestBody = IntegrationTestHelper
        .readJsonFile("employee_register_normalUser.json", RegisterEmployeeRequest.class, "json", "request");
    requestBody.setEmailAddress("admin");


    this.mockMvc.perform(MockMvcRequestBuilders.post(EmployeeControllerPath.BASE + EmployeeControllerPath.REGISTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(IntegrationTestHelper.convertToByte(requestBody)))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fields", Matchers.hasKey(RegisterEmployeeRequest.Fields.emailAddress)));
  }

  @Test
  @WithMockCustomUser(roles = { EmployeeRole.ROLE_ADMIN })
  public void register_RequestBodyPasswordIsInvalid_IsBadRequest() throws Exception {
    Clinic clinic = this.constructBasicClinic();
    this.clinicRepository.save(clinic);

    RegisterEmployeeRequest requestBody = IntegrationTestHelper
        .readJsonFile("employee_register_normalUser.json", RegisterEmployeeRequest.class, "json", "request");
    requestBody.setPassword("notStrongPassword");

    this.mockMvc.perform(MockMvcRequestBuilders.post(EmployeeControllerPath.BASE + EmployeeControllerPath.REGISTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(IntegrationTestHelper.convertToByte(requestBody)))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fields", Matchers.hasKey(RegisterEmployeeRequest.Fields.password)));
  }

  @Test
  @WithMockCustomUser(roles = { EmployeeRole.ROLE_ADMIN })
  public void register_RequestBodyFirstNameIsNull_IsBadRequest() throws Exception {
    Clinic clinic = this.constructBasicClinic();
    this.clinicRepository.save(clinic);

    RegisterEmployeeRequest requestBody = IntegrationTestHelper
        .readJsonFile("employee_register_normalUser.json", RegisterEmployeeRequest.class, "json", "request");
    requestBody.setFirstName(null);

    this.mockMvc.perform(MockMvcRequestBuilders.post(EmployeeControllerPath.BASE + EmployeeControllerPath.REGISTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(IntegrationTestHelper.convertToByte(requestBody)))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fields", Matchers.hasKey(RegisterEmployeeRequest.Fields.firstName)));
  }

  @Test
  @WithMockCustomUser(roles = { EmployeeRole.ROLE_ADMIN })
  public void register_RequestBodyGenderIsInvalid_IsBadRequest() throws Exception {
    Clinic clinic = this.constructBasicClinic();
    this.clinicRepository.save(clinic);

    RegisterEmployeeRequest requestBody = IntegrationTestHelper
        .readJsonFile("employee_register_normalUser.json", RegisterEmployeeRequest.class, "json", "request");
    requestBody.setGender("unknown");

    this.mockMvc.perform(MockMvcRequestBuilders.post(EmployeeControllerPath.BASE + EmployeeControllerPath.REGISTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(IntegrationTestHelper.convertToByte(requestBody)))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fields", Matchers.hasKey(RegisterEmployeeRequest.Fields.gender)));
  }

  @Test
  @WithMockCustomUser(roles = { EmployeeRole.ROLE_ADMIN })
  public void register_RequestBodyAgeIsInvalid_IsBadRequest() throws Exception {
    Clinic clinic = this.constructBasicClinic();
    this.clinicRepository.save(clinic);

    RegisterEmployeeRequest requestBody = IntegrationTestHelper
        .readJsonFile("employee_register_normalUser.json", RegisterEmployeeRequest.class, "json", "request");
    requestBody.setAge(1);

    this.mockMvc.perform(MockMvcRequestBuilders.post(EmployeeControllerPath.BASE + EmployeeControllerPath.REGISTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(IntegrationTestHelper.convertToByte(requestBody)))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fields", Matchers.hasKey(RegisterEmployeeRequest.Fields.age)));
  }

  @Test
  @WithMockCustomUser(roles = { EmployeeRole.ROLE_ADMIN })
  public void register_AddressIsNull_IsBadRequest() throws Exception {
    Clinic clinic = this.constructBasicClinic();
    this.clinicRepository.save(clinic);

    RegisterEmployeeRequest requestBody = IntegrationTestHelper
        .readJsonFile("employee_register_normalUser.json", RegisterEmployeeRequest.class, "json", "request");
    requestBody.setAddress(null);

    this.mockMvc.perform(MockMvcRequestBuilders.post(EmployeeControllerPath.BASE + EmployeeControllerPath.REGISTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(IntegrationTestHelper.convertToByte(requestBody)))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fields", Matchers.hasKey(RegisterEmployeeRequest.Fields.address)));
  }


  @Test
  @WithMockCustomUser(roles = { EmployeeRole.ROLE_ADMIN })
  public void register_PhoneNumberIsNull_IsBadRequest() throws Exception {
    Clinic clinic = this.constructBasicClinic();
    this.clinicRepository.save(clinic);

    RegisterEmployeeRequest requestBody = IntegrationTestHelper
        .readJsonFile("employee_register_normalUser.json", RegisterEmployeeRequest.class, "json", "request");
    requestBody.setPhoneNumber(null);

    this.mockMvc.perform(MockMvcRequestBuilders.post(EmployeeControllerPath.BASE + EmployeeControllerPath.REGISTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(IntegrationTestHelper.convertToByte(requestBody)))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fields", Matchers.hasKey(RegisterEmployeeRequest.Fields.phoneNumber)));
  }

  @Test
  @WithMockCustomUser(roles = { EmployeeRole.ROLE_ADMIN })
  public void register_EmployeeRoleCreationIsInvalid_IsBadRequest() throws Exception {
    Clinic clinic = this.constructBasicClinic();
    this.clinicRepository.save(clinic);

    RegisterEmployeeRequest requestBody = IntegrationTestHelper
        .readJsonFile("employee_register_normalUser.json", RegisterEmployeeRequest.class, "json", "request");
    requestBody.setRole(EmployeeRole.ROLE_DEVELOPER);

    this.mockMvc.perform(MockMvcRequestBuilders.post(EmployeeControllerPath.BASE + EmployeeControllerPath.REGISTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(IntegrationTestHelper.convertToByte(requestBody)))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fields", Matchers.hasKey(RegisterEmployeeRequest.Fields.role)));
  }

  @Test
  @WithMockCustomUser(roles = { EmployeeRole.ROLE_ADMIN })
  public void register_EmployeeTypeDoctorHasNoDoctorNumber_IsBadRequest() throws Exception {
    Clinic clinic = this.constructBasicClinic();
    this.clinicRepository.save(clinic);

    RegisterEmployeeRequest requestBody = IntegrationTestHelper
        .readJsonFile("employee_register_normalUser.json", RegisterEmployeeRequest.class, "json", "request");
    requestBody.setType(EmployeeType.DOCTOR);
    requestBody.setDoctorNumber(null);

    this.mockMvc.perform(MockMvcRequestBuilders.post(EmployeeControllerPath.BASE + EmployeeControllerPath.REGISTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(IntegrationTestHelper.convertToByte(requestBody)))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fields", Matchers.hasKey(RegisterEmployeeRequest.Fields.doctorNumber)));
  }


}
