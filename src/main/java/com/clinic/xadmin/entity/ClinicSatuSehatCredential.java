package com.clinic.xadmin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.vault.repository.mapping.Secret;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Secret(value = "satu.sehat.credential")
public class ClinicSatuSehatCredential implements Serializable {

  @Id
  private String clinicId;
  private String satuSehatOrganizationKey;
  private String satuSehatClientKey;
  private String satuSehatSecretKey;

}
