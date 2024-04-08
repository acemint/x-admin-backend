package com.clinic.xadmin.controller;

import com.clinic.xadmin.constant.employee.EmployeeRole;
import com.clinic.xadmin.constant.employee.EmployeeType;
import com.clinic.xadmin.controller.employee.EmployeeControllerPath;
import com.clinic.xadmin.dto.request.employee.RegisterEmployeeRequest;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.helper.IntegrationTestHelper;
import com.clinic.xadmin.helper.WithMockCustomUser;
import com.clinic.xadmin.repository.clinic.ClinicRepository;
import com.clinic.xadmin.repository.employee.EmployeeRepository;
import com.clinic.xadmin.security.authprovider.CustomUserDetails;
import com.clinic.xadmin.security.context.AppSecurityContextHolder;
import com.fasterxml.jackson.core.type.TypeReference;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
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
  private AppSecurityContextHolder appSecurityContextHolder;

  @BeforeEach
  public void beforeEach() {

  }

  @AfterEach
  public void afterEach() {
    this.employeeRepository.deleteAll();
    this.clinicRepository.deleteAll();
  }

  private Clinic register_ConstructClinic(String specificFilePath) {
    String filePath = "employee_register.json";
    if (Objects.nonNull(specificFilePath)) {
      filePath = specificFilePath;
    }
    Clinic clinic = IntegrationTestHelper.readJsonFile(filePath, Clinic.class, IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.ENTITY_HINT);
    return clinic;
  }

  private ArrayList<Employee> filter_SaveClinicAndEmployees(String specificFilePath) {
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
    ArrayList<Employee> employees = this.filter_SaveClinicAndEmployees(null);

    this.mockMvc.perform(MockMvcRequestBuilders.get(EmployeeControllerPath.BASE + EmployeeControllerPath.FILTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(5)));
  }

  @Test
  @WithMockCustomUser(clinicId = "234", roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void filter_EmployeeCannotAccessOtherClinic_IsOk() throws Exception {
    ArrayList<Employee> employees = this.filter_SaveClinicAndEmployees(null);

    this.mockMvc.perform(MockMvcRequestBuilders.get(EmployeeControllerPath.BASE + EmployeeControllerPath.FILTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(0)));
  }

  @Test
  @WithMockCustomUser(clinicId = "", roles = { EmployeeRole.ROLE_DEVELOPER})
  public void filter_CurrentUserIsDeveloper_EmployeeAccessOwnClinicData_IsOk() throws Exception {
    ArrayList<Employee> employees = this.filter_SaveClinicAndEmployees(null);

    this.mockMvc.perform(MockMvcRequestBuilders.get(EmployeeControllerPath.BASE + EmployeeControllerPath.FILTER)
            .param("clinicCode", "CLC-123")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(5)));
  }

  @Test
  @WithMockCustomUser(clinicId = "123", roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void filter_RequestParameterNameIsNotEmpty_IsOk() throws Exception {
    ArrayList<Employee> employees = this.filter_SaveClinicAndEmployees(null);

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
    ArrayList<Employee> employees = this.filter_SaveClinicAndEmployees(null);

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
    ArrayList<Employee> employees = this.filter_SaveClinicAndEmployees(null);

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
    ArrayList<Employee> employees = this.filter_SaveClinicAndEmployees(null);

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
    ArrayList<Employee> employees = this.filter_SaveClinicAndEmployees(null);

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
  public void filter_CurrentUserIsUnauthenticated_IsForbidden() throws Exception {

    this.mockMvc.perform(MockMvcRequestBuilders.get(EmployeeControllerPath.BASE + EmployeeControllerPath.FILTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
  }

  @Test
  @WithMockCustomUser(clinicId = "123", roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void filter_CurrentUserIsNotDeveloper_ClinicCodeParameterIsIgnored_IsForbidden() throws Exception {
    this.mockMvc.perform(MockMvcRequestBuilders.get(EmployeeControllerPath.BASE + EmployeeControllerPath.FILTER)
            .param("clinicCode", "CLC-123")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
  }



  @Test
  @WithMockCustomUser(roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void register_EmployeeRoleIsClinicAdmin_IsOk() throws Exception {
    Clinic clinic = this.register_ConstructClinic(null);
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
  @WithMockCustomUser(roles = { EmployeeRole.ROLE_DEVELOPER})
  public void register_CurrentUserIsDeveloper_EmployeeRoleIsClinicAdmin_IsOk() throws Exception {
    Clinic clinic = this.register_ConstructClinic(null);
    this.clinicRepository.save(clinic);
    byte[] requestBody = IntegrationTestHelper
        .readJsonAsBytes("employee_register_normalUser.json", IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);

    this.mockMvc.perform(MockMvcRequestBuilders.post(EmployeeControllerPath.BASE + EmployeeControllerPath.REGISTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .param("clinicCode", "CLC-123")
            .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.notNullValue()));
    List<Employee> savedEmployees = this.employeeRepository.findAll();
    Assertions.assertEquals(savedEmployees.size(), 1);
    Assertions.assertEquals(savedEmployees.get(0).getClinic().getCode(), "CLC-123");
    Assertions.assertEquals(savedEmployees.get(0).getEmailAddress(), "master@gmail.com");
  }

  @Test
  @WithMockCustomUser(roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void register_EmployeeRoleIsClinicAdminAndRegisteredEmployeeTypeIsDoctor_IsOk() throws Exception {
    Clinic clinic = this.register_ConstructClinic(null);
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
  public void register_CurrentUserIsNonDeveloper_ClinicCodeRequestParameterIsNotNull_IsForbidden() throws Exception {
    byte[] requestBody = IntegrationTestHelper
        .readJsonAsBytes("employee_register_normalUserTypeIsDoctor.json", IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);

    this.mockMvc.perform(MockMvcRequestBuilders.post(EmployeeControllerPath.BASE + EmployeeControllerPath.REGISTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .param("clinicCode", "CLC-123")
            .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
  }

  @Test
  @WithMockCustomUser(roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void register_RequestBodyEmailAddressIsInvalid_IsBadRequest() throws Exception {
    Clinic clinic = this.register_ConstructClinic(null);
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
    Clinic clinic = this.register_ConstructClinic(null);
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
    Clinic clinic = this.register_ConstructClinic(null);
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
    Clinic clinic = this.register_ConstructClinic(null);
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
    Clinic clinic = this.register_ConstructClinic(null);
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
    Clinic clinic = this.register_ConstructClinic(null);
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
    Clinic clinic = this.register_ConstructClinic(null);
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
    Clinic clinic = this.register_ConstructClinic(null);
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
    Clinic clinic = this.register_ConstructClinic(null);
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
