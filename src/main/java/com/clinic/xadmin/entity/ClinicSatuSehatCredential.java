package com.clinic.xadmin.entity;

import com.clinic.xadmin.configuration.VaultConfiguration;
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
@Secret(value = ClinicSatuSehatCredential.KEY_SPACE, backend = VaultConfiguration.DEFAULT_SECRET_FOLDER_PATH)
public class ClinicSatuSehatCredential implements Serializable {

  public static final String KEY_SPACE = "satu.sehat.credential";

  @Id
  private String clinicId;
  private String satuSehatOrganizationKey;
  private String satuSehatClientKey;
  private String satuSehatSecretKey;

}