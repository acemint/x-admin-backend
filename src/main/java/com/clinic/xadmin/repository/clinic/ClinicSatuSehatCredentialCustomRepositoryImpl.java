package com.clinic.xadmin.repository.clinic;

import com.clinic.xadmin.configuration.ObjectMapperConfiguration;
import com.clinic.xadmin.configuration.VaultConfiguration;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.ClinicSatuSehatCredential;
import com.clinic.xadmin.exception.XAdminInternalException;
import com.clinic.xadmin.property.ClinicProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.vault.core.VaultKeyValueOperationsSupport;
import org.springframework.vault.core.VaultOperations;
import org.springframework.vault.support.VaultResponse;

import java.util.Map;
import java.util.Objects;

public class ClinicSatuSehatCredentialCustomRepositoryImpl implements ClinicSatuSehatCredentialCustomRepository {

  private final VaultOperations vaultOperations;
  private final ClinicRepository clinicRepository;
  private final ObjectMapper objectMapper;
  private final ClinicProperty clinicProperty;

  @Autowired
  public ClinicSatuSehatCredentialCustomRepositoryImpl(VaultOperations vaultOperations, ClinicRepository clinicRepository,
      @Qualifier(value = ObjectMapperConfiguration.OBJECT_MAPPER_DEFAULT_BEAN_NAME) ObjectMapper objectMapper,
      ClinicProperty clinicProperty) {
    this.vaultOperations = vaultOperations;
    this.clinicRepository = clinicRepository;
    this.objectMapper = objectMapper;
    this.clinicProperty = clinicProperty;
  }

  @Override
  public ClinicSatuSehatCredential searchMainClinicApp() {
    Clinic clinic = this.clinicRepository.searchByName(clinicProperty.getName());
    if (Objects.isNull(clinic)) {
      throw new XAdminInternalException("Clinic not found " + clinicProperty.getName());
    }
    VaultResponse vaultResponse = this.vaultOperations.opsForKeyValue(VaultConfiguration.DEFAULT_SECRET_FOLDER_PATH, VaultKeyValueOperationsSupport.KeyValueBackend.KV_2)
        .get(ClinicSatuSehatCredential.KEY_SPACE + "/" + clinic.getCode());
    if (Objects.isNull(vaultResponse)) {
      throw new XAdminInternalException("Clinic auth not found " + clinicProperty.getName());
    }
    // So that it won't create new secret in vault if for some reason this response is saved
    vaultResponse.setRequestId(clinic.getCode());
    return this.objectMapper.convertValue(vaultResponse.getRequiredData(), ClinicSatuSehatCredential.class);
  }
}
