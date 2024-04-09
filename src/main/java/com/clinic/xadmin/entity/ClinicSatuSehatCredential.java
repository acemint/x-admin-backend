package com.clinic.xadmin.entity;

import lombok.Builder;
import org.springframework.vault.repository.mapping.Secret;

@Builder
@Secret
public class ClinicSatuSehatCredential {

  private String clinicId;
  private String satuSehatOrganizationKey;
  private String satuSehatClientKey;
  private String satuSehatSecretKey;

}
