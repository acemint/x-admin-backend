package com.clinic.xadmin.controller;

import com.clinic.xadmin.constant.EmployeeRole;
import com.clinic.xadmin.controller.employee.EmployeeControllerPath;
import com.clinic.xadmin.dto.response.employee.EmployeeResponse;
import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.helper.IntegrationTestHelper;
import com.clinic.xadmin.helper.WithMockCustomUser;
import com.clinic.xadmin.repository.employee.EmployeeRepository;
import com.clinic.xadmin.security.authprovider.CustomUserDetails;
import com.clinic.xadmin.security.configuration.PasswordEncoderConfiguration;
import com.clinic.xadmin.security.context.AppSecurityContextHolder;
import org.junit.jupiter.api.AfterEach;
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

public class EmployeeControllerTest extends BaseControllerTest {

  @Autowired
  private EmployeeRepository employeeRepository;

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
  }

  private Employee constructBasicEmployee() {
    return Employee.builder()
        .emailAddress("user@gmail.com")
        .password(this.passwordEncoder.encode("Test123:>"))
        .role(EmployeeRole.ROLE_REGULAR_EMPLOYEE)
        .build();
  }

  @Test
  public void login_Valid_Success() throws Exception {
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
        .andExpect(MockMvcResultMatchers.jsonPath("$.emailAddress").value(expectedResponseBody.getEmailAddress()));
  }

  @Test
  public void login_InvalidPassword_Error() throws Exception {
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
  public void getSelf_EmployeeHasLoggedIn_Success() throws Exception {
    Authentication authentication = this.appSecurityContextHolder.getCurrentContext().getAuthentication();
    CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

    this.mockMvc.perform(MockMvcRequestBuilders.get(EmployeeControllerPath.BASE + EmployeeControllerPath.SELF)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.emailAddress").value(customUserDetails.getEmployee().getEmailAddress()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.role").value(customUserDetails.getEmployee().getRole()));
  }

  @Test
  public void getSelf_EmployeeHasNotLoggedIn_Error() throws Exception {
    this.mockMvc.perform(MockMvcRequestBuilders.get(EmployeeControllerPath.BASE + EmployeeControllerPath.SELF)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
  }


}
