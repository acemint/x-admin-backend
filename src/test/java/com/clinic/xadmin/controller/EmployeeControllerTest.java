package com.clinic.xadmin.controller;

import com.clinic.xadmin.constant.employee.EmployeeRole;
import com.clinic.xadmin.constant.employee.EmployeeStatus;
import com.clinic.xadmin.constant.employee.EmployeeType;
import com.clinic.xadmin.constant.experimental.Gender;
import com.clinic.xadmin.controller.employee.EmployeeControllerPath;
import com.clinic.xadmin.dto.request.employee.LoginEmployeeRequest;
import com.clinic.xadmin.dto.request.employee.RegisterEmployeeRequest;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.helper.IntegrationTestHelper;
import com.clinic.xadmin.helper.WithMockCustomUser;
import com.clinic.xadmin.repository.clinic.ClinicRepository;
import com.clinic.xadmin.repository.employee.EmployeeRepository;
import com.clinic.xadmin.security.authprovider.CustomUserDetails;
import com.clinic.xadmin.security.configuration.PasswordEncoderConfiguration;
import com.clinic.xadmin.security.context.AppSecurityContextHolder;
import com.fasterxml.jackson.core.type.TypeReference;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        .username("user.clc001")
        .firstName("user")
        .emailAddress("user@gmail.com")
        .code("123")
        .password(this.passwordEncoder.encode("Test123:>"))
        .phoneNumber("0896")
        .address("jl. random")
        .gender(Gender.MALE)
        .age(18)
        .role(EmployeeRole.ROLE_CLINIC_ADMIN)
        .status(EmployeeStatus.ACTIVE)
        .build();
  }

  private Clinic constructBasicClinic() {
    return Clinic.builder()
        .id("123")
        .name("Dental")
        .code("CLC-123")
        .build();
  }

  private ArrayList<Employee> constructBasicEmployeesFromClinicForFilter(String specificFilePath) {
    String filePath = "employee_filter.json";
    if (Objects.nonNull(specificFilePath)) {
      filePath = specificFilePath;
    }
    Clinic clinic = IntegrationTestHelper.readJsonFile(filePath, Clinic.class, IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.ENTITY_HINT);
    this.clinicRepository.save(clinic);
    ArrayList<Employee> employees = IntegrationTestHelper.readJsonFileFromSpecificPath(filePath, "$.employees", new TypeReference<>(){},
        IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.ENTITY_HINT);
    employees.forEach(e -> e.setClinic(clinic));
    this.employeeRepository.saveAll(employees);
    return employees;
  }

  @Test
  public void login_Valid_IsOk() throws Exception {
    Employee employee = this.constructBasicEmployee();
    this.employeeRepository.save(employee);

    byte[] requestBody = IntegrationTestHelper
        .readJsonAsBytes("employee_login_normalUser.json", IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);

    this.mockMvc.perform(MockMvcRequestBuilders.post(EmployeeControllerPath.BASE + EmployeeControllerPath.LOGIN)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content.emailAddress").value(employee.getEmailAddress()));
  }

  @Test
  public void login_InvalidPassword_IsForbidden() throws Exception {
    Employee employee = this.constructBasicEmployee();
    this.employeeRepository.save(employee);

    LoginEmployeeRequest requestBody = IntegrationTestHelper
        .readJsonFile("employee_login_normalUser.json", LoginEmployeeRequest.class, IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);
    requestBody.setPassword("random123:>");

    this.mockMvc.perform(MockMvcRequestBuilders.post(EmployeeControllerPath.BASE + EmployeeControllerPath.LOGIN)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(IntegrationTestHelper.convertToByte(requestBody)))
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
        .andExpect(MockMvcResultMatchers.jsonPath("$.content.username").value(customUserDetails.getEmployee().getUsername()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content.role").value(customUserDetails.getEmployee().getRole()));
  }

  @Test
  public void getSelf_EmployeeHasNotLoggedIn_IsForbidden() throws Exception {
    this.mockMvc.perform(MockMvcRequestBuilders.get(EmployeeControllerPath.BASE + EmployeeControllerPath.SELF)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
  }



  @Test
  @WithMockCustomUser(clinicId = "123", roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void filter_EmployeeAccessOwnClinicData_IsOk() throws Exception {
    ArrayList<Employee> employees = this.constructBasicEmployeesFromClinicForFilter(null);

    this.mockMvc.perform(MockMvcRequestBuilders.get(EmployeeControllerPath.BASE + EmployeeControllerPath.FILTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(5)));
  }

  @Test
  @WithMockCustomUser(clinicId = "234", roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void filter_EmployeeCannotAccessOtherClinic_IsOk() throws Exception {
    ArrayList<Employee> employees = this.constructBasicEmployeesFromClinicForFilter(null);

    this.mockMvc.perform(MockMvcRequestBuilders.get(EmployeeControllerPath.BASE + EmployeeControllerPath.FILTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(0)));
  }

  @Test
  @WithMockCustomUser(clinicId = "123", roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void filter_RequestParameterNameIsNotEmpty_IsOk() throws Exception {
    ArrayList<Employee> employees = this.constructBasicEmployeesFromClinicForFilter(null);

    this.mockMvc.perform(MockMvcRequestBuilders.get(EmployeeControllerPath.BASE + EmployeeControllerPath.FILTER)
            .param("name", "teri")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].emailAddress",
            Matchers.hasItems(
                employees.get(3).getEmailAddress(),
                employees.get(4).getEmailAddress())
        ));
  }

  @Test
  @WithMockCustomUser(clinicId = "123", roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void filter_SortByName_IsOk() throws Exception {
    ArrayList<Employee> employees = this.constructBasicEmployeesFromClinicForFilter(null);

    this.mockMvc.perform(MockMvcRequestBuilders.get(EmployeeControllerPath.BASE + EmployeeControllerPath.FILTER)
            .param("name", "user")
            .param("sortBy", "name")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].emailAddress",
            Matchers.containsInRelativeOrder(
                employees.stream().filter(e -> e.getUsername().equals("user1_123")).findFirst().get().getEmailAddress(),
                employees.stream().filter(e -> e.getUsername().equals("user2_123")).findFirst().get().getEmailAddress(),
                employees.stream().filter(e -> e.getUsername().equals("user3_123")).findFirst().get().getEmailAddress())
        ));
  }

  @Test
  @WithMockCustomUser(clinicId = "123", roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void filter_SortByType_IsOk() throws Exception {
    ArrayList<Employee> employees = this.constructBasicEmployeesFromClinicForFilter(null);

    this.mockMvc.perform(MockMvcRequestBuilders.get(EmployeeControllerPath.BASE + EmployeeControllerPath.FILTER)
            .param("name", "user")
            .param("sortBy", "type")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        // the first 2 employee data has "type" null which is why the predictable order is only the third
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1:3].emailAddress",
            Matchers.containsInAnyOrder(
                employees.stream().filter(e -> e.getUsername().equals("user1_123")).findFirst().get().getEmailAddress(),
                employees.stream().filter(e -> e.getUsername().equals("user3_123")).findFirst().get().getEmailAddress()
        )))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].emailAddress",
            Matchers.equalTo(employees.stream().filter(e -> e.getUsername().equals("user2_123")).findFirst().get().getEmailAddress())
        ));
  }

  @Test
  @WithMockCustomUser(clinicId = "123", roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void filter_SortByStatus_IsOk() throws Exception {
    ArrayList<Employee> employees = this.constructBasicEmployeesFromClinicForFilter(null);

    this.mockMvc.perform(MockMvcRequestBuilders.get(EmployeeControllerPath.BASE + EmployeeControllerPath.FILTER)
            .param("name", "user")
            .param("sortBy", "status")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0:2].emailAddress",
            Matchers.containsInAnyOrder(
                employees.stream().filter(e -> e.getUsername().equals("user1_123")).findFirst().get().getEmailAddress(),
                employees.stream().filter(e -> e.getUsername().equals("user2_123")).findFirst().get().getEmailAddress()
            )))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].emailAddress",
            Matchers.equalTo(employees.stream().filter(e -> e.getUsername().equals("user3_123")).findFirst().get().getEmailAddress())
        ));
  }

  @Test
  @WithMockCustomUser(clinicId = "123", roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void filter_PageNumberIsNotDefaultAndPageSizeIsNotDefault_IsOk() throws Exception {
    ArrayList<Employee> employees = this.constructBasicEmployeesFromClinicForFilter(null);

    this.mockMvc.perform(MockMvcRequestBuilders.get(EmployeeControllerPath.BASE + EmployeeControllerPath.FILTER)
            .param("pageNumber", "1")
            .param("pageSize", "3")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(2)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.paginationMetadata.totalPages", Matchers.equalTo(2)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.paginationMetadata.currentElementSize", Matchers.equalTo(2)));
  }



  @Test
  @WithMockCustomUser(roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void register_EmployeeRoleIsClinicAdmin_IsOk() throws Exception {
    Clinic clinic = this.constructBasicClinic();
    this.clinicRepository.save(clinic);
    byte[] requestBody = IntegrationTestHelper
        .readJsonAsBytes("employee_register_normalUser.json", IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);

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
  @WithMockCustomUser(roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void register_EmployeeRoleIsClinicAdminAndRegisteredEmployeeTypeIsDoctor_IsOk() throws Exception {
    Clinic clinic = this.constructBasicClinic();
    this.clinicRepository.save(clinic);
    byte[] requestBody = IntegrationTestHelper
        .readJsonAsBytes("employee_register_normalUserTypeIsDoctor.json", IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);

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
        .readJsonAsBytes("employee_register_normalUser.json", IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);

    this.mockMvc.perform(MockMvcRequestBuilders.post(EmployeeControllerPath.BASE + EmployeeControllerPath.REGISTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
  }

  @Test
  @WithMockCustomUser(roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void register_RequestBodyEmailAddressIsInvalid_IsBadRequest() throws Exception {
    Clinic clinic = this.constructBasicClinic();
    this.clinicRepository.save(clinic);

    RegisterEmployeeRequest requestBody = IntegrationTestHelper
        .readJsonFile("employee_register_normalUser.json", RegisterEmployeeRequest.class, IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);
    requestBody.setEmailAddress("admin");


    this.mockMvc.perform(MockMvcRequestBuilders.post(EmployeeControllerPath.BASE + EmployeeControllerPath.REGISTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(IntegrationTestHelper.convertToByte(requestBody)))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fields", Matchers.hasKey(RegisterEmployeeRequest.Fields.emailAddress)));
  }

  @Test
  @WithMockCustomUser(roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void register_RequestBodyPasswordIsInvalid_IsBadRequest() throws Exception {
    Clinic clinic = this.constructBasicClinic();
    this.clinicRepository.save(clinic);

    RegisterEmployeeRequest requestBody = IntegrationTestHelper
        .readJsonFile("employee_register_normalUser.json", RegisterEmployeeRequest.class, IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);
    requestBody.setPassword("notStrongPassword");

    this.mockMvc.perform(MockMvcRequestBuilders.post(EmployeeControllerPath.BASE + EmployeeControllerPath.REGISTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(IntegrationTestHelper.convertToByte(requestBody)))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fields", Matchers.hasKey(RegisterEmployeeRequest.Fields.password)));
  }

  @Test
  @WithMockCustomUser(roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void register_RequestBodyFirstNameIsNull_IsBadRequest() throws Exception {
    Clinic clinic = this.constructBasicClinic();
    this.clinicRepository.save(clinic);

    RegisterEmployeeRequest requestBody = IntegrationTestHelper
        .readJsonFile("employee_register_normalUser.json", RegisterEmployeeRequest.class, IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);
    requestBody.setFirstName(null);

    this.mockMvc.perform(MockMvcRequestBuilders.post(EmployeeControllerPath.BASE + EmployeeControllerPath.REGISTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(IntegrationTestHelper.convertToByte(requestBody)))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fields", Matchers.hasKey(RegisterEmployeeRequest.Fields.firstName)));
  }

  @Test
  @WithMockCustomUser(roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void register_RequestBodyGenderIsInvalid_IsBadRequest() throws Exception {
    Clinic clinic = this.constructBasicClinic();
    this.clinicRepository.save(clinic);

    RegisterEmployeeRequest requestBody = IntegrationTestHelper
        .readJsonFile("employee_register_normalUser.json", RegisterEmployeeRequest.class, IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);
    requestBody.setGender("unknown");

    this.mockMvc.perform(MockMvcRequestBuilders.post(EmployeeControllerPath.BASE + EmployeeControllerPath.REGISTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(IntegrationTestHelper.convertToByte(requestBody)))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fields", Matchers.hasKey(RegisterEmployeeRequest.Fields.gender)));
  }

  @Test
  @WithMockCustomUser(roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void register_RequestBodyAgeIsInvalid_IsBadRequest() throws Exception {
    Clinic clinic = this.constructBasicClinic();
    this.clinicRepository.save(clinic);

    RegisterEmployeeRequest requestBody = IntegrationTestHelper
        .readJsonFile("employee_register_normalUser.json", RegisterEmployeeRequest.class, IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);
    requestBody.setAge(1);

    this.mockMvc.perform(MockMvcRequestBuilders.post(EmployeeControllerPath.BASE + EmployeeControllerPath.REGISTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(IntegrationTestHelper.convertToByte(requestBody)))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fields", Matchers.hasKey(RegisterEmployeeRequest.Fields.age)));
  }

  @Test
  @WithMockCustomUser(roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void register_AddressIsNull_IsBadRequest() throws Exception {
    Clinic clinic = this.constructBasicClinic();
    this.clinicRepository.save(clinic);

    RegisterEmployeeRequest requestBody = IntegrationTestHelper
        .readJsonFile("employee_register_normalUser.json", RegisterEmployeeRequest.class, IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);
    requestBody.setAddress(null);

    this.mockMvc.perform(MockMvcRequestBuilders.post(EmployeeControllerPath.BASE + EmployeeControllerPath.REGISTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(IntegrationTestHelper.convertToByte(requestBody)))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fields", Matchers.hasKey(RegisterEmployeeRequest.Fields.address)));
  }


  @Test
  @WithMockCustomUser(roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void register_PhoneNumberIsNull_IsBadRequest() throws Exception {
    Clinic clinic = this.constructBasicClinic();
    this.clinicRepository.save(clinic);

    RegisterEmployeeRequest requestBody = IntegrationTestHelper
        .readJsonFile("employee_register_normalUser.json", RegisterEmployeeRequest.class, IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);
    requestBody.setPhoneNumber(null);

    this.mockMvc.perform(MockMvcRequestBuilders.post(EmployeeControllerPath.BASE + EmployeeControllerPath.REGISTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(IntegrationTestHelper.convertToByte(requestBody)))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fields", Matchers.hasKey(RegisterEmployeeRequest.Fields.phoneNumber)));
  }

  @Test
  @WithMockCustomUser(roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void register_EmployeeRoleCreationIsInvalid_IsBadRequest() throws Exception {
    Clinic clinic = this.constructBasicClinic();
    this.clinicRepository.save(clinic);

    RegisterEmployeeRequest requestBody = IntegrationTestHelper
        .readJsonFile("employee_register_normalUser.json", RegisterEmployeeRequest.class, IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);
    requestBody.setRole(EmployeeRole.ROLE_DEVELOPER);

    this.mockMvc.perform(MockMvcRequestBuilders.post(EmployeeControllerPath.BASE + EmployeeControllerPath.REGISTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(IntegrationTestHelper.convertToByte(requestBody)))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fields", Matchers.hasKey(RegisterEmployeeRequest.Fields.role)));
  }


  @Test
  @WithMockCustomUser(roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void register_EmployeeTypeDoctorHasNoDoctorNumber_IsBadRequest() throws Exception {
    Clinic clinic = this.constructBasicClinic();
    this.clinicRepository.save(clinic);

    RegisterEmployeeRequest requestBody = IntegrationTestHelper
        .readJsonFile("employee_register_normalUserTypeIsDoctor.json", RegisterEmployeeRequest.class, IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);
    requestBody.setType(EmployeeType.SPECIALIST_DOCTOR);
    requestBody.setDoctorNumber(null);
    requestBody.setPracticeLicense(null);

    this.mockMvc.perform(MockMvcRequestBuilders.post(EmployeeControllerPath.BASE + EmployeeControllerPath.REGISTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(IntegrationTestHelper.convertToByte(requestBody)))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fields", Matchers.hasKey(RegisterEmployeeRequest.Fields.doctorNumber)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fields", Matchers.hasKey(RegisterEmployeeRequest.Fields.practiceLicense)));
  }


}
