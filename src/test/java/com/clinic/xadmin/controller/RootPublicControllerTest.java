package com.clinic.xadmin.controller;

import com.clinic.xadmin.controller.root.RootPublicControllerPath;
import com.clinic.xadmin.dto.request.member.LoginMemberRequest;
import com.clinic.xadmin.entity.Member;
import com.clinic.xadmin.helper.IntegrationTestHelper;
import com.clinic.xadmin.repository.clinic.ClinicRepository;
import com.clinic.xadmin.repository.member.MemberRepository;
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
  private MemberRepository memberRepository;

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
    this.memberRepository.deleteAll();
    this.clinicRepository.deleteAll();
  }

  private Member login_constructBasicMember(String specificFilePath) {
    String filePath = "member_login.json";
    if (Objects.nonNull(specificFilePath)) {
      filePath = specificFilePath;
    }
    ArrayList<Member>
        members = IntegrationTestHelper.readJsonFileFromSpecificPath(filePath, "$.members", new TypeReference<>(){},
        IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.ENTITY_HINT);
    members.forEach(e -> e.setPassword(this.passwordEncoder.encode(e.getPassword())));
    this.memberRepository.saveAll(members);
    return members.get(0);
  }

  @Test
  public void login_Valid_IsOk() throws Exception {
    Member member = this.login_constructBasicMember(null);
    this.memberRepository.save(member);

    byte[] requestBody = IntegrationTestHelper
        .readJsonAsBytes("member_login_normalUser.json", IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);

    this.mockMvc.perform(MockMvcRequestBuilders.post(RootPublicControllerPath.BASE + RootPublicControllerPath.LOGIN)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content.emailAddress").value(member.getEmailAddress()));
  }

  @Test
  public void login_InvalidPassword_IsForbidden() throws Exception {
    Member member = this.login_constructBasicMember(null);
    this.memberRepository.save(member);

    LoginMemberRequest requestBody = IntegrationTestHelper
        .readJsonFile("member_login_normalUser.json", LoginMemberRequest.class, IntegrationTestHelper.JSON_HINT, IntegrationTestHelper.REQUEST_HINT);
    requestBody.setPassword("random123:>");

    this.mockMvc.perform(MockMvcRequestBuilders.post(RootPublicControllerPath.BASE + RootPublicControllerPath.LOGIN)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(IntegrationTestHelper.convertToByte(requestBody)))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
  }

}
