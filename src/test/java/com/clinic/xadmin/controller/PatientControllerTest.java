package com.clinic.xadmin.controller;

import com.clinic.xadmin.constant.employee.EmployeeRole;
import com.clinic.xadmin.controller.patient.PatientControllerPath;
import com.clinic.xadmin.dto.request.patient.RegisterPatientRequest;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Patient;
import com.clinic.xadmin.helper.IntegrationTestHelper;
import com.clinic.xadmin.helper.WithMockCustomUser;
import com.clinic.xadmin.repository.clinic.ClinicRepository;
import com.clinic.xadmin.repository.patient.PatientRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PatientControllerTest extends BaseControllerTest {

  @Autowired
  private PatientRepository patientRepository;

  @Autowired
  private ClinicRepository clinicRepository;

  @BeforeEach
  public void beforeEach() {

  }

  @AfterEach
  public void afterEach() {
    this.patientRepository.deleteAll();
    this.clinicRepository.deleteAll();
  }

  private void register_ConstructClinic(String specificFilePath) {
    String filePath = "patient_register.json";
    if (Objects.nonNull(specificFilePath)) {
      filePath = specificFilePath;
    }
    Clinic clinic = IntegrationTestHelper.readJsonFile(filePath, Clinic.class, IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.ENTITY_HINT);
    this.clinicRepository.save(clinic);
  }


  private void filter_constructEmployees(String specificFilePath) {
    String filePath = "patient_filter.json";
    if (Objects.nonNull(specificFilePath)) {
      filePath = specificFilePath;
    }
    Clinic clinic = IntegrationTestHelper.readJsonFile(filePath, Clinic.class, IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.ENTITY_HINT);
    this.clinicRepository.save(clinic);
    ArrayList<Patient> patients = IntegrationTestHelper.readJsonFileFromSpecificPath(filePath, "$.patients", new TypeReference<>(){},
        IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.ENTITY_HINT);
    patients.forEach(e -> e.setClinic(clinic));
    this.patientRepository.saveAll(patients);
  }



  @Test
  @WithMockCustomUser(clinicId = "123", roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void filter_Valid_IsOk() throws Exception {
    this.filter_constructEmployees(null);

    this.mockMvc.perform(MockMvcRequestBuilders.get(PatientControllerPath.BASE + PatientControllerPath.FILTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(3)));
  }

  @Test
  @WithMockCustomUser(clinicId = "234", roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void filter_EmployeeCannotAccessOtherClinic_IsOk() throws Exception {
    this.filter_constructEmployees(null);

    this.mockMvc.perform(MockMvcRequestBuilders.get(PatientControllerPath.BASE + PatientControllerPath.FILTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(0)));
  }

  @Test
  @WithMockCustomUser(clinicId = "123", roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void filter_RequestParameterNameIsNotNull_IsOk() throws Exception {
    this.filter_constructEmployees(null);

    this.mockMvc.perform(MockMvcRequestBuilders.get(PatientControllerPath.BASE + PatientControllerPath.FILTER)
            .param("name", "uzu")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(1)));
  }

  @Test
  @WithMockCustomUser(clinicId = "123", roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void filter_SortByName_IsOk() throws Exception {
    this.filter_constructEmployees(null);
    List<Patient> patients = this.patientRepository.findAll();

    this.mockMvc.perform(MockMvcRequestBuilders.get(PatientControllerPath.BASE + PatientControllerPath.FILTER)
            .param("sortBy", "name")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].code",
            Matchers.containsInRelativeOrder(
                patients.stream().map(Patient::getCode).filter(e -> e.equals("PAT-1")).findFirst().get(),
                patients.stream().map(Patient::getCode).filter(e -> e.equals("PAT-3")).findFirst().get(),
                patients.stream().map(Patient::getCode).filter(e -> e.equals("PAT-2")).findFirst().get())
        ));
  }


  @Test
  @WithMockCustomUser(clinicId = "123", roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void filter_PageNumberIsNotDefaultAndPageSizeIsNotDefault_IsOk() throws Exception {
    this.filter_constructEmployees(null);

    this.mockMvc.perform(MockMvcRequestBuilders.get(PatientControllerPath.BASE + PatientControllerPath.FILTER)
            .param("pageNumber", "1")
            .param("pageSize", "2")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.paginationMetadata.totalPages", Matchers.equalTo(2)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.paginationMetadata.currentElementSize", Matchers.equalTo(1)));
  }

  @Test
  public void filter_CurrentUserIsNotAuthenticated_IsForbidden() throws Exception {
    this.filter_constructEmployees(null);

    this.mockMvc.perform(MockMvcRequestBuilders.get(PatientControllerPath.BASE + PatientControllerPath.FILTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
  }

  @Test
  @WithMockCustomUser(clinicId = "123", roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void filter_CurrentUserIsNonDeveloper_ClinicCodeRequestParameterIsNotNull_IsForbidden() throws Exception {
    this.mockMvc.perform(MockMvcRequestBuilders.get(PatientControllerPath.BASE + PatientControllerPath.FILTER)
            .param("clinicCode", "CLC-123")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
  }

  @Test
  @WithMockCustomUser(clinicId = "123", roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void register_Valid_Success() throws Exception {
    this.register_ConstructClinic(null);
    RegisterPatientRequest request = IntegrationTestHelper
        .readJsonFile("patient_register_normalUser.json", RegisterPatientRequest.class, IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);

    this.mockMvc.perform(MockMvcRequestBuilders.post(PatientControllerPath.BASE + PatientControllerPath.REGISTER)
            .content(IntegrationTestHelper.convertToByte(request))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content.emailAddress").value("master@gmail.com"));
  }


  @Test
  @WithMockCustomUser(clinicId = "123", roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void register_CurrentUserIsNonDeveloper_ClinicCodeRequestParameterIsNotNull_IsForbidden() throws Exception {
    this.register_ConstructClinic(null);
    RegisterPatientRequest request = IntegrationTestHelper
        .readJsonFile("patient_register_normalUser.json", RegisterPatientRequest.class, IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);

    this.mockMvc.perform(MockMvcRequestBuilders.post(PatientControllerPath.BASE + PatientControllerPath.REGISTER)
            .content(IntegrationTestHelper.convertToByte(request))
            .param("clinicCode", "CLC-123")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));

  }




}
