package com.clinic.xadmin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.vault.repository.mapping.Secret;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Secret
public class ClinicSatuSehatCredential {

  @Id
  private String clinicId;
  private String satuSehatOrganizationKey;
  private String satuSehatClientKey;
  private String satuSehatSecretKey;

}
