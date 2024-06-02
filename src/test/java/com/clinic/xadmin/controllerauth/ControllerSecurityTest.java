package com.clinic.xadmin.controllerauth;

import com.clinic.xadmin.constant.member.MemberRole;
import com.clinic.xadmin.controller.BaseControllerTest;
import com.clinic.xadmin.helper.WithMockCustomUser;
import com.clinic.xadmin.security.constant.SecurityAuthorizationType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

public class ControllerSecurityTest extends BaseControllerTest {

  @TestConfiguration
  public static class TestingControllerConfiguration {

    @RestController
    @RequestMapping("/test")
    public class TestingController {

      @GetMapping(value = "/is-developer", produces = MediaType.APPLICATION_JSON_VALUE)
      @PreAuthorize(SecurityAuthorizationType.IS_DEVELOPER)
      public ResponseEntity<String> isDeveloperController() {
        return ResponseEntity.ok().body("Success");
      }

      @GetMapping(value = "/is-clinic-admin", produces = MediaType.APPLICATION_JSON_VALUE)
      @PreAuthorize(SecurityAuthorizationType.IS_CLINIC_ADMIN)
      public ResponseEntity<String> isClinicAdmin() {
        return ResponseEntity.ok().body("Success");
      }

      @GetMapping(value = "/is-authenticated", produces = MediaType.APPLICATION_JSON_VALUE)
      @PreAuthorize(SecurityAuthorizationType.IS_FULLY_AUTHENTICATED)
      public ResponseEntity<String> isAuthenticated() {
        return ResponseEntity.ok().body("Success");
      }

    }

  }

  // Section: isAuthenticated test
  @Test
  @WithMockCustomUser(roles = {"-"})
  public void isAuthenticated_UserIsAuthenticated_IsOk() throws Exception {
    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/test/is-authenticated")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
  }

  @Test
  public void isAuthenticated_UserIsNotAuthenticated_IsForbidden() throws Exception {
    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/test/is-authenticated")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
  }

  // Section: isDeveloper test
  @Test
  @WithMockCustomUser(roles = { MemberRole.ROLE_DEVELOPER } )
  public void isDeveloper_UserIsDeveloper_IsOk() throws Exception {
    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/test/is-developer")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
  }

  @Test
  public void isDeveloper_UserIsNotAuthenticated_IsForbidden() throws Exception {
    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/test/is-developer")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
  }


  @Test
  @WithMockCustomUser(roles = { MemberRole.ROLE_CLINIC_ADMIN } )
  public void isDeveloper_UserIsClinicAdmin_IsForbidden() throws Exception {
    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/test/is-developer")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
  }

  @Test
  @WithMockCustomUser(roles = { MemberRole.ROLE_PATIENT } )
  public void isDeveloper_UserIsPatient_IsForbidden() throws Exception {
    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/test/is-developer")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
  }



}
