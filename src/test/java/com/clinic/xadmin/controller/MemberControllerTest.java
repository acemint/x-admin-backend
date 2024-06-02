package com.clinic.xadmin.controller;

import com.clinic.xadmin.constant.member.MemberRole;
import com.clinic.xadmin.controller.member.MemberControllerPath;
import com.clinic.xadmin.dto.request.member.RegisterMemberAsManagerRequest;
import com.clinic.xadmin.dto.request.member.RegisterMemberRequest;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Member;
import com.clinic.xadmin.helper.IntegrationTestHelper;
import com.clinic.xadmin.helper.WithMockCustomUser;
import com.clinic.xadmin.repository.clinic.ClinicRepository;
import com.clinic.xadmin.repository.member.MemberRepository;
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

public class MemberControllerTest extends BaseControllerTest {

  @Autowired
  private MemberRepository mmemberRepository;

  @Autowired
  private ClinicRepository clinicRepository;

  @Autowired
  private AppSecurityContextHolder appSecurityContextHolder;

  @BeforeEach
  public void beforeEach() {

  }

  @AfterEach
  public void afterEach() {
    this.mmemberRepository.deleteAll();
    this.clinicRepository.deleteAll();
  }

  private Clinic register_ConstructClinic(String specificFilePath) {
    String filePath = "member_register.json";
    if (Objects.nonNull(specificFilePath)) {
      filePath = specificFilePath;
    }
    Clinic clinic = IntegrationTestHelper.readJsonFile(filePath, Clinic.class, IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.ENTITY_HINT);
    return clinic;
  }

  private ArrayList<Member> filter_SaveClinicAndMembers(String specificFilePath) {
    String filePath = "member_filter.json";
    if (Objects.nonNull(specificFilePath)) {
      filePath = specificFilePath;
    }
    Clinic clinic = IntegrationTestHelper.readJsonFile(filePath, Clinic.class, IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.ENTITY_HINT);
    this.clinicRepository.save(clinic);
    ArrayList<Member>
        members = IntegrationTestHelper.readJsonFileFromSpecificPath(filePath, "$.Members", new TypeReference<>(){},
        IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.ENTITY_HINT);
    members.forEach(e -> e.setClinic(clinic));
    this.mmemberRepository.saveAll(members);
    return members;
  }


  @Test
  @WithMockCustomUser()
  public void getSelf_MemberHasLoggedIn_IsOk() throws Exception {
    Authentication authentication = this.appSecurityContextHolder.getCurrentContext().getAuthentication();
    CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

    this.mockMvc.perform(MockMvcRequestBuilders.get(
                MemberControllerPath.BASE + MemberControllerPath.SELF)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content.username").value(customUserDetails.getMember().getClinicUsername()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content.role").value(customUserDetails.getMember().getRole()));
  }

  @Test
  public void getSelf_MemberHasNotLoggedIn_IsForbidden() throws Exception {
    this.mockMvc.perform(MockMvcRequestBuilders.get(
                MemberControllerPath.BASE + MemberControllerPath.SELF)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
  }



  @Test
  @WithMockCustomUser(clinicId = "123", roles = { MemberRole.ROLE_CLINIC_ADMIN})
  public void filter_MemberAccessOwnClinicData_IsOk() throws Exception {
    ArrayList<Member> members = this.filter_SaveClinicAndMembers(null);

    this.mockMvc.perform(MockMvcRequestBuilders.get(
                MemberControllerPath.BASE + MemberControllerPath.FILTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(5)));
  }

  @Test
  @WithMockCustomUser(clinicId = "234", roles = { MemberRole.ROLE_CLINIC_ADMIN})
  public void filter_MemberCannotAccessOtherClinic_IsOk() throws Exception {
    ArrayList<Member> members = this.filter_SaveClinicAndMembers(null);

    this.mockMvc.perform(MockMvcRequestBuilders.get(
                MemberControllerPath.BASE + MemberControllerPath.FILTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(0)));
  }

  @Test
  @WithMockCustomUser(clinicId = "", roles = { MemberRole.ROLE_DEVELOPER})
  public void filter_CurrentUserIsDeveloper_MemberAccessOwnClinicData_IsOk() throws Exception {
    ArrayList<Member> members = this.filter_SaveClinicAndMembers(null);

    this.mockMvc.perform(MockMvcRequestBuilders.get(
                MemberControllerPath.BASE + MemberControllerPath.FILTER)
            .param("clinicCode", "CLC-123")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(5)));
  }

  @Test
  @WithMockCustomUser(clinicId = "123", roles = { MemberRole.ROLE_CLINIC_ADMIN})
  public void filter_RequestParameterNameIsNotEmpty_IsOk() throws Exception {
    ArrayList<Member> members = this.filter_SaveClinicAndMembers(null);

    this.mockMvc.perform(MockMvcRequestBuilders.get(
                MemberControllerPath.BASE + MemberControllerPath.FILTER)
            .param("name", "teri")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].emailAddress",
            Matchers.hasItems(
                members.get(3).getEmailAddress(),
                members.get(4).getEmailAddress())
        ));
  }

  @Test
  @WithMockCustomUser(clinicId = "123", roles = { MemberRole.ROLE_CLINIC_ADMIN})
  public void filter_SortByName_IsOk() throws Exception {
    ArrayList<Member> members = this.filter_SaveClinicAndMembers(null);

    this.mockMvc.perform(MockMvcRequestBuilders.get(
                MemberControllerPath.BASE + MemberControllerPath.FILTER)
            .param("name", "user")
            .param("sortBy", "name")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].emailAddress",
            Matchers.containsInRelativeOrder(
                members.stream().filter(e -> e.getClinicUsername().equals("user1_123")).findFirst().get().getEmailAddress(),
                members.stream().filter(e -> e.getClinicUsername().equals("user2_123")).findFirst().get().getEmailAddress(),
                members.stream().filter(e -> e.getClinicUsername().equals("user3_123")).findFirst().get().getEmailAddress())
        ));
  }

  @Test
  @WithMockCustomUser(clinicId = "123", roles = { MemberRole.ROLE_CLINIC_ADMIN})
  public void filter_SortByType_IsOk() throws Exception {
    ArrayList<Member> members = this.filter_SaveClinicAndMembers(null);

    this.mockMvc.perform(MockMvcRequestBuilders.get(
                MemberControllerPath.BASE + MemberControllerPath.FILTER)
            .param("name", "user")
            .param("sortBy", "type")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        // the first 2 Member data has "type" null which is why the predictable order is only the third
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1:3].emailAddress",
            Matchers.containsInAnyOrder(
                members.stream().filter(e -> e.getClinicUsername().equals("user1_123")).findFirst().get().getEmailAddress(),
                members.stream().filter(e -> e.getClinicUsername().equals("user3_123")).findFirst().get().getEmailAddress()
        )))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].emailAddress",
            Matchers.equalTo(members.stream().filter(e -> e.getClinicUsername().equals("user2_123")).findFirst().get().getEmailAddress())
        ));
  }

  @Test
  @WithMockCustomUser(clinicId = "123", roles = { MemberRole.ROLE_CLINIC_ADMIN})
  public void filter_SortByStatus_IsOk() throws Exception {
    ArrayList<Member> members = this.filter_SaveClinicAndMembers(null);

    this.mockMvc.perform(MockMvcRequestBuilders.get(
                MemberControllerPath.BASE + MemberControllerPath.FILTER)
            .param("name", "user")
            .param("sortBy", "status")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0:2].emailAddress",
            Matchers.containsInAnyOrder(
                members.stream().filter(e -> e.getClinicUsername().equals("user1_123")).findFirst().get().getEmailAddress(),
                members.stream().filter(e -> e.getClinicUsername().equals("user2_123")).findFirst().get().getEmailAddress()
            )))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].emailAddress",
            Matchers.equalTo(members.stream().filter(e -> e.getClinicUsername().equals("user3_123")).findFirst().get().getEmailAddress())
        ));
  }

  @Test
  @WithMockCustomUser(clinicId = "123", roles = { MemberRole.ROLE_CLINIC_ADMIN})
  public void filter_PageNumberIsNotDefaultAndPageSizeIsNotDefault_IsOk() throws Exception {
    ArrayList<Member> members = this.filter_SaveClinicAndMembers(null);

    this.mockMvc.perform(MockMvcRequestBuilders.get(
                MemberControllerPath.BASE + MemberControllerPath.FILTER)
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

    this.mockMvc.perform(MockMvcRequestBuilders.get(
                MemberControllerPath.BASE + MemberControllerPath.FILTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
  }

  @Test
  @WithMockCustomUser(clinicId = "123", roles = { MemberRole.ROLE_CLINIC_ADMIN})
  public void filter_CurrentUserIsNotDeveloper_ClinicCodeRequestParameterIsIgnored_IsForbidden() throws Exception {
    this.mockMvc.perform(MockMvcRequestBuilders.get(
                MemberControllerPath.BASE + MemberControllerPath.FILTER)
            .param("clinicCode", "CLC-123")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
  }



  @Test
  @WithMockCustomUser(roles = { MemberRole.ROLE_CLINIC_ADMIN})
  public void register_MemberRoleIsClinicAdmin_IsOk() throws Exception {
    Clinic clinic = this.register_ConstructClinic(null);
    this.clinicRepository.save(clinic);
    byte[] requestBody = IntegrationTestHelper
        .readJsonAsBytes("member_register_normalUser.json", IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);

    this.mockMvc.perform(MockMvcRequestBuilders.post(
                MemberControllerPath.BASE + MemberControllerPath.REGISTER_PRACTITIONER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.notNullValue()));
    List<Member> savedMembers = this.mmemberRepository.findAll();
    Assertions.assertEquals(savedMembers.size(), 1);
    Assertions.assertEquals(savedMembers.get(0).getClinic().getId(), "123");
    Assertions.assertEquals(savedMembers.get(0).getEmailAddress(), "master@gmail.com");
  }

  @Test
  @WithMockCustomUser(roles = { MemberRole.ROLE_DEVELOPER})
  public void register_CurrentUserIsDeveloper_MemberRoleIsClinicAdmin_IsOk() throws Exception {
    Clinic clinic = this.register_ConstructClinic(null);
    this.clinicRepository.save(clinic);
    byte[] requestBody = IntegrationTestHelper
        .readJsonAsBytes("member_register_normalUser.json", IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);

    this.mockMvc.perform(MockMvcRequestBuilders.post(
                MemberControllerPath.BASE + MemberControllerPath.REGISTER_PRACTITIONER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .param("clinicCode", "CLC-123")
            .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.notNullValue()));
    List<Member> savedMembers = this.mmemberRepository.findAll();
    Assertions.assertEquals(savedMembers.size(), 1);
    Assertions.assertEquals(savedMembers.get(0).getClinic().getCode(), "CLC-123");
    Assertions.assertEquals(savedMembers.get(0).getEmailAddress(), "master@gmail.com");
  }

  @Test
  @WithMockCustomUser(roles = { MemberRole.ROLE_CLINIC_ADMIN})
  public void register_MemberRoleIsClinicAdminAndRegisteredMemberTypeIsDoctor_IsOk() throws Exception {
    Clinic clinic = this.register_ConstructClinic(null);
    this.clinicRepository.save(clinic);
    byte[] requestBody = IntegrationTestHelper
        .readJsonAsBytes("member_register_normalUserTypeIsDoctor.json", IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);

    this.mockMvc.perform(MockMvcRequestBuilders.post(
                MemberControllerPath.BASE + MemberControllerPath.REGISTER_PRACTITIONER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.notNullValue()));
    List<Member> savedMembers = this.mmemberRepository.findAll();
    Assertions.assertEquals(savedMembers.size(), 1);
    Assertions.assertEquals(savedMembers.get(0).getClinic().getId(), "123");
    Assertions.assertEquals(savedMembers.get(0).getEmailAddress(), "master@gmail.com");
  }

  @Test
  @WithMockCustomUser(roles = { MemberRole.ROLE_PATIENT})
  public void register_MemberRoleIsRegularMember_IsForbidden() throws Exception {
    byte[] requestBody = IntegrationTestHelper
        .readJsonAsBytes("member_register_normalUser.json", IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);

    this.mockMvc.perform(MockMvcRequestBuilders.post(
                MemberControllerPath.BASE + MemberControllerPath.REGISTER_PRACTITIONER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
  }

  @Test
  @WithMockCustomUser(roles = { MemberRole.ROLE_CLINIC_ADMIN})
  public void register_CurrentUserIsNonDeveloper_ClinicCodeRequestParameterIsNotNull_IsForbidden() throws Exception {
    byte[] requestBody = IntegrationTestHelper
        .readJsonAsBytes("member_register_normalUserTypeIsDoctor.json", IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);

    this.mockMvc.perform(MockMvcRequestBuilders.post(
                MemberControllerPath.BASE + MemberControllerPath.REGISTER_PRACTITIONER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .param("clinicCode", "CLC-123")
            .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
  }

  @Test
  @WithMockCustomUser(roles = { MemberRole.ROLE_CLINIC_ADMIN})
  public void register_RequestBodyEmailAddressIsInvalid_IsBadRequest() throws Exception {
    Clinic clinic = this.register_ConstructClinic(null);
    this.clinicRepository.save(clinic);

    RegisterMemberRequest requestBody = IntegrationTestHelper
        .readJsonFile("member_register_normalUser.json", RegisterMemberRequest.class, IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);
    requestBody.setEmailAddress("admin");


    this.mockMvc.perform(MockMvcRequestBuilders.post(
                MemberControllerPath.BASE + MemberControllerPath.REGISTER_PRACTITIONER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(IntegrationTestHelper.convertToByte(requestBody)))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fields", Matchers.hasKey(RegisterMemberRequest.Fields.emailAddress)));
  }

  @Test
  @WithMockCustomUser(roles = { MemberRole.ROLE_CLINIC_ADMIN})
  public void register_RequestBodyPasswordIsInvalid_IsBadRequest() throws Exception {
    Clinic clinic = this.register_ConstructClinic(null);
    this.clinicRepository.save(clinic);

    RegisterMemberAsManagerRequest requestBody = IntegrationTestHelper
        .readJsonFile("member_register_normalUser.json", RegisterMemberAsManagerRequest.class, IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);
    requestBody.setPassword("notStrongPassword");

    this.mockMvc.perform(MockMvcRequestBuilders.post(
                MemberControllerPath.BASE + MemberControllerPath.REGISTER_PRACTITIONER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(IntegrationTestHelper.convertToByte(requestBody)))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fields", Matchers.hasKey(RegisterMemberAsManagerRequest.Fields.password)));
  }

  @Test
  @WithMockCustomUser(roles = { MemberRole.ROLE_CLINIC_ADMIN})
  public void register_RequestBodyFirstNameIsNull_IsBadRequest() throws Exception {
    Clinic clinic = this.register_ConstructClinic(null);
    this.clinicRepository.save(clinic);

    RegisterMemberRequest requestBody = IntegrationTestHelper
        .readJsonFile("member_register_normalUser.json", RegisterMemberRequest.class, IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);
    requestBody.setFirstName(null);

    this.mockMvc.perform(MockMvcRequestBuilders.post(
                MemberControllerPath.BASE + MemberControllerPath.REGISTER_PRACTITIONER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(IntegrationTestHelper.convertToByte(requestBody)))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fields", Matchers.hasKey(RegisterMemberRequest.Fields.firstName)));
  }

  @Test
  @WithMockCustomUser(roles = { MemberRole.ROLE_CLINIC_ADMIN})
  public void register_RequestBodyGenderIsInvalid_IsBadRequest() throws Exception {
    Clinic clinic = this.register_ConstructClinic(null);
    this.clinicRepository.save(clinic);

    RegisterMemberRequest requestBody = IntegrationTestHelper
        .readJsonFile("member_register_normalUser.json", RegisterMemberRequest.class, IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);
    requestBody.setGender("unknown");

    this.mockMvc.perform(MockMvcRequestBuilders.post(
                MemberControllerPath.BASE + MemberControllerPath.REGISTER_PRACTITIONER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(IntegrationTestHelper.convertToByte(requestBody)))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fields", Matchers.hasKey(RegisterMemberRequest.Fields.gender)));
  }

  @Test
  @WithMockCustomUser(roles = { MemberRole.ROLE_CLINIC_ADMIN})
  public void register_RequestBodyAgeIsInvalid_IsBadRequest() throws Exception {
    Clinic clinic = this.register_ConstructClinic(null);
    this.clinicRepository.save(clinic);

    RegisterMemberRequest requestBody = IntegrationTestHelper
        .readJsonFile("member_register_normalUser.json", RegisterMemberRequest.class, IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);
    requestBody.setDateOfBirth("2020-09-01");

    this.mockMvc.perform(MockMvcRequestBuilders.post(
                MemberControllerPath.BASE + MemberControllerPath.REGISTER_PRACTITIONER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(IntegrationTestHelper.convertToByte(requestBody)))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fields", Matchers.hasKey(RegisterMemberRequest.Fields.dateOfBirth)));
  }

  @Test
  @WithMockCustomUser(roles = { MemberRole.ROLE_CLINIC_ADMIN})
  public void register_AddressIsNull_IsBadRequest() throws Exception {
    Clinic clinic = this.register_ConstructClinic(null);
    this.clinicRepository.save(clinic);

    RegisterMemberRequest requestBody = IntegrationTestHelper
        .readJsonFile("member_register_normalUser.json", RegisterMemberRequest.class, IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);
    requestBody.setAddress(null);

    this.mockMvc.perform(MockMvcRequestBuilders.post(
                MemberControllerPath.BASE + MemberControllerPath.REGISTER_PRACTITIONER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(IntegrationTestHelper.convertToByte(requestBody)))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fields", Matchers.hasKey(RegisterMemberRequest.Fields.address)));
  }


  @Test
  @WithMockCustomUser(roles = { MemberRole.ROLE_CLINIC_ADMIN})
  public void register_PhoneNumberIsNull_IsBadRequest() throws Exception {
    Clinic clinic = this.register_ConstructClinic(null);
    this.clinicRepository.save(clinic);

    RegisterMemberRequest requestBody = IntegrationTestHelper
        .readJsonFile("member_register_normalUser.json", RegisterMemberRequest.class, IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);
    requestBody.setPhoneNumber(null);

    this.mockMvc.perform(MockMvcRequestBuilders.post(
                MemberControllerPath.BASE + MemberControllerPath.REGISTER_PRACTITIONER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(IntegrationTestHelper.convertToByte(requestBody)))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fields", Matchers.hasKey(RegisterMemberRequest.Fields.phoneNumber)));
  }

  @Test
  @WithMockCustomUser(roles = { MemberRole.ROLE_CLINIC_ADMIN})
  public void register_MemberRoleCreationIsInvalid_IsBadRequest() throws Exception {
    Clinic clinic = this.register_ConstructClinic(null);
    this.clinicRepository.save(clinic);

    RegisterMemberAsManagerRequest requestBody = IntegrationTestHelper
        .readJsonFile("member_register_normalUser.json", RegisterMemberAsManagerRequest.class, IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);
    requestBody.setRole(MemberRole.ROLE_DEVELOPER);

    this.mockMvc.perform(MockMvcRequestBuilders.post(
                MemberControllerPath.BASE + MemberControllerPath.REGISTER_PRACTITIONER)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(IntegrationTestHelper.convertToByte(requestBody)))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.fields", Matchers.hasKey(RegisterMemberAsManagerRequest.Fields.role)));
  }


}
