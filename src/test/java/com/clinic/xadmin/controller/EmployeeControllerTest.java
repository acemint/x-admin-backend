package com.clinic.xadmin.controller;

import com.clinic.xadmin.constant.EmployeeRole;
import com.clinic.xadmin.controller.constant.CookieName;
import com.clinic.xadmin.controller.employee.EmployeeControllerPath;
import com.clinic.xadmin.dto.request.employee.LoginEmployeeRequest;
import com.clinic.xadmin.dto.response.employee.EmployeeResponse;
import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.helper.IntegrationTestHelper;
import com.clinic.xadmin.repository.employee.EmployeeRepository;
import com.clinic.xadmin.security.configuration.PasswordEncoderConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

@AutoConfigureWebTestClient
public class EmployeeControllerTest extends BaseControllerTest {

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  @Qualifier(value = PasswordEncoderConfiguration.BEAN_NAME)
  private PasswordEncoder passwordEncoder;

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
  public void login_Valid_Success() {
    Employee employee = this.constructBasicEmployee();
    this.employeeRepository.save(employee);

    LoginEmployeeRequest requestBody = IntegrationTestHelper
        .readJsonFile("employee_login_normalUser.json", LoginEmployeeRequest.class, "json", "request");

    EmployeeResponse expectedResponseBody = IntegrationTestHelper
        .readJsonFile("employee_login.json", EmployeeResponse.class,  "json", "response");

    EmployeeResponse responseEntity = this.webTestClient.post()
        .uri(EmployeeControllerPath.BASE + EmployeeControllerPath.LOGIN)
        .bodyValue(requestBody)
        .exchange()
        .expectStatus().isEqualTo(HttpStatus.OK)
        .expectCookie().exists(CookieName.CREDENTIAL)
        .expectBody(EmployeeResponse.class)
        .returnResult()
        .getResponseBody();
    Assertions.assertNotNull(responseEntity);
    Assertions.assertEquals(expectedResponseBody.getEmailAddress(), responseEntity.getEmailAddress());
  }

  @Test
  public void login_InvalidPassword_Success() {
    Employee employee = this.constructBasicEmployee();
    this.employeeRepository.save(employee);

    LoginEmployeeRequest requestBody = IntegrationTestHelper
        .readJsonFile("employee_login_normalUserWrongPassword.json", LoginEmployeeRequest.class, "json", "request");

    this.webTestClient.post()
        .uri(EmployeeControllerPath.BASE + EmployeeControllerPath.LOGIN)
        .bodyValue(requestBody)
        .exchange()
        .expectStatus().isEqualTo(HttpStatus.FORBIDDEN)
        .expectCookie().doesNotExist(CookieName.CREDENTIAL);
  }


}
