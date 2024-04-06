package com.clinic.xadmin.controller;

import com.clinic.xadmin.constant.employee.EmployeeRole;
import com.clinic.xadmin.constant.experimental.Gender;
import com.clinic.xadmin.controller.patient.PatientControllerPath;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Patient;
import com.clinic.xadmin.helper.WithMockCustomUser;
import com.clinic.xadmin.repository.clinic.ClinicRepository;
import com.clinic.xadmin.repository.patient.PatientRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

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

  private ArrayList<Patient> constructBasicPatients(Clinic clinic) {
    ArrayList<Patient> patients = new ArrayList<>();
    patients.add(Patient.builder()
        .firstName("madara")
        .lastName("uchiha")
        .lastName("madara.uchiha@gmail.com")
        .code("123")
        .age(24)
        .gender(Gender.MALE)
        .clinic(clinic)
        .build());
    patients.add(Patient.builder()
        .firstName("sasuke")
        .lastName("uchiha")
        .lastName("sasuke.uchiha@gmail.com")
        .code("123")
        .age(18)
        .gender(Gender.MALE)
        .clinic(clinic)
        .build());
    patients.add(Patient.builder()
        .firstName("naruto")
        .lastName("uzumaki")
        .emailAddress("naruto.uzumaki@gmail.com")
        .code("123")
        .age(18)
        .gender(Gender.MALE)
        .clinic(clinic)
        .build());
    return patients;
  }

  private Clinic constructBasicClinic() {
    return Clinic.builder()
        .id("123")
        .build();
  }

  @Test
  @WithMockCustomUser(clinicId = "123", roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void filter_Valid_IsOk() throws Exception {
    Clinic clinic = this.constructBasicClinic();
    this.clinicRepository.save(clinic);
    ArrayList<Patient> patients = this.constructBasicPatients(clinic);
    this.patientRepository.saveAll(patients);

    this.mockMvc.perform(MockMvcRequestBuilders.get(PatientControllerPath.BASE + PatientControllerPath.FILTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(3)));
  }

  @Test
  @WithMockCustomUser(clinicId = "123", roles = { EmployeeRole.ROLE_REGULAR_EMPLOYEE })
  public void filter_EmployeeRoleIsNotAdmin_IsForbidden() throws Exception {
    Clinic clinic = this.constructBasicClinic();
    this.clinicRepository.save(clinic);
    ArrayList<Patient> patients = this.constructBasicPatients(clinic);
    this.patientRepository.saveAll(patients);

    this.mockMvc.perform(MockMvcRequestBuilders.get(PatientControllerPath.BASE + PatientControllerPath.FILTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
  }

  @Test
  @WithMockCustomUser(clinicId = "234", roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void filter_EmployeeCannotAccessOtherClinic_IsOk() throws Exception {
    Clinic clinic = this.constructBasicClinic();
    this.clinicRepository.save(clinic);
    ArrayList<Patient> patients = this.constructBasicPatients(clinic);
    this.patientRepository.saveAll(patients);

    this.mockMvc.perform(MockMvcRequestBuilders.get(PatientControllerPath.BASE + PatientControllerPath.FILTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(0)));
  }

  @Test
  @WithMockCustomUser(clinicId = "123", roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void filter_RequestParameterNameIsNotNull_IsOk() throws Exception {
    Clinic clinic = this.constructBasicClinic();
    this.clinicRepository.save(clinic);
    ArrayList<Patient> patients = this.constructBasicPatients(clinic);
    this.patientRepository.saveAll(patients);

    this.mockMvc.perform(MockMvcRequestBuilders.get(PatientControllerPath.BASE + PatientControllerPath.FILTER)
            .param("name", "uzu")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(1)));
  }

  @Test
  @WithMockCustomUser(clinicId = "123", roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void filter_SortByName_IsOk() throws Exception {
    Clinic clinic = this.constructBasicClinic();
    this.clinicRepository.save(clinic);
    ArrayList<Patient> patients = this.constructBasicPatients(clinic);
    this.patientRepository.saveAll(patients);

    this.mockMvc.perform(MockMvcRequestBuilders.get(PatientControllerPath.BASE + PatientControllerPath.FILTER)
            .param("sortBy", "name")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].emailAddress",
            Matchers.containsInRelativeOrder(
                patients.get(0).getEmailAddress(),
                patients.get(2).getEmailAddress(),
                patients.get(1).getEmailAddress())
        ));
  }


  @Test
  @WithMockCustomUser(clinicId = "123", roles = { EmployeeRole.ROLE_CLINIC_ADMIN})
  public void filter_PageNumberIsNotDefaultAndPageSizeIsNotDefault_IsOk() throws Exception {
    Clinic clinic = this.constructBasicClinic();
    this.clinicRepository.save(clinic);
    ArrayList<Patient> patients = this.constructBasicPatients(clinic);
    this.patientRepository.saveAll(patients);

    this.mockMvc.perform(MockMvcRequestBuilders.get(PatientControllerPath.BASE + PatientControllerPath.FILTER)
            .param("pageNumber", "1")
            .param("pageSize", "2")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.paginationMetadata.totalPages", Matchers.equalTo(2)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.paginationMetadata.currentElementSize", Matchers.equalTo(1)));
  }

}
