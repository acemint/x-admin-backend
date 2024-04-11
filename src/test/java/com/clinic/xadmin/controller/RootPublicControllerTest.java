package com.clinic.xadmin.controller;

import com.clinic.xadmin.controller.employee.EmployeeControllerPath;
import com.clinic.xadmin.controller.root.RootPublicControllerPath;
import com.clinic.xadmin.dto.request.employee.LoginEmployeeRequest;
import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.helper.IntegrationTestHelper;
import com.clinic.xadmin.repository.clinic.ClinicRepository;
import com.clinic.xadmin.repository.employee.EmployeeRepository;
import com.clinic.xadmin.security.configuration.PasswordEncoderConfiguration;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Objects;

public class RootPublicControllerTest extends BaseControllerTest {

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private ClinicRepository clinicRepository;

  @Autowired
  @Qualifier(value = PasswordEncoderConfiguration.BEAN_NAME)
  private PasswordEncoder passwordEncoder;

  @BeforeEach
  public void beforeEach() {

  }

  @AfterEach
  public void afterEach() {
    this.employeeRepository.deleteAll();
    this.clinicRepository.deleteAll();
  }

  private Employee login_constructBasicEmployee(String specificFilePath) {
    String filePath = "employee_login.json";
    if (Objects.nonNull(specificFilePath)) {
      filePath = specificFilePath;
    }
    ArrayList<Employee> employees = IntegrationTestHelper.readJsonFileFromSpecificPath(filePath, "$.employees", new TypeReference<>(){},
        IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.ENTITY_HINT);
    employees.forEach(e -> e.setPassword(this.passwordEncoder.encode(e.getPassword())));
    this.employeeRepository.saveAll(employees);
    return employees.get(0);
  }

  @Test
  public void login_Valid_IsOk() throws Exception {
    Employee employee = this.login_constructBasicEmployee(null);
    this.employeeRepository.save(employee);

    byte[] requestBody = IntegrationTestHelper
        .readJsonAsBytes("employee_login_normalUser.json", IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);

    this.mockMvc.perform(MockMvcRequestBuilders.post(RootPublicControllerPath.BASE + RootPublicControllerPath.LOGIN)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content.emailAddress").value(employee.getEmailAddress()));
  }

  @Test
  public void login_InvalidPassword_IsForbidden() throws Exception {
    Employee employee = this.login_constructBasicEmployee(null);
    this.employeeRepository.save(employee);

    LoginEmployeeRequest requestBody = IntegrationTestHelper
        .readJsonFile("employee_login_normalUser.json", LoginEmployeeRequest.class, IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);
    requestBody.setPassword("random123:>");

    this.mockMvc.perform(MockMvcRequestBuilders.post(RootPublicControllerPath.BASE + RootPublicControllerPath.LOGIN)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(IntegrationTestHelper.convertToByte(requestBody)))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
  }

}
